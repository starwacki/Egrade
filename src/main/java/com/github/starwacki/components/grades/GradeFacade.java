package com.github.starwacki.components.grades;
import com.github.starwacki.components.grades.dto.GradeDTO;
import com.github.starwacki.components.grades.dto.GradeViewDTO;
import com.github.starwacki.components.grades.dto.SubjectDTO;
import com.github.starwacki.components.grades.exceptions.StudentNotFoundException;
import com.github.starwacki.components.grades.exceptions.SubjectNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Service
@AllArgsConstructor
public class GradeFacade {

    private final GradeRepository gradeRepository;


    public SubjectDTO getOneSubjectGrades(int studentId, int subjectId) {
        return gradeRepository
                .findAllByStudentIDAndGradeSubject(studentId,getGradeSubjectByOrdinal(subjectId))
                .map(grades -> mapListGradeToSubjectDTO(getGradeSubjectByOrdinal(subjectId).toString(),grades))
                .orElseThrow(() -> new StudentNotFoundException(studentId));
    }

    public List<SubjectDTO> getAllGradesByStudentID(int studentId) {
        return   gradeRepository.findAllByStudentID(studentId)
                .map(grades -> getAllStudentGradesInList(grades))
                .orElseThrow(() -> new StudentNotFoundException(studentId));
    }

    public GradeViewDTO getOneGrade(int studentId, int gradeID) {
        return gradeRepository
                .findByStudentIDAndId(studentId,gradeID)
                .map(grade -> GradeMapper.mapGradeToGradeViewDTO(grade))
                .orElseThrow(() -> new StudentNotFoundException(studentId));
    }

    public GradeDTO addGradeToStudent(GradeDTO gradeDTO) {
         gradeRepository.save(GradeMapper
                   .mapGradeDTOToGrade(
                      gradeDTO,
                      getDegreeBySymbol(gradeDTO.degree())));
         return gradeDTO;
    }

    public GradeDTO updateGrade(int studentID, int gradeID, GradeDTO gradeDTO) {
        return gradeRepository.findByStudentIDAndId(studentID,gradeID)
                .map(grade -> GradeMapper.mapGradeToGradeDTO(changeGradeInformationAndSave(grade,gradeDTO)))
                .orElseThrow(() -> new StudentNotFoundException(studentID));
    }

    public GradeDTO deleteStudentGrade(int studentId, int gradeID) {
        return gradeRepository
                .findByStudentIDAndId(studentId,gradeID)
                .map(grade -> GradeMapper.mapGradeToGradeDTO(deleteGrade(grade)))
                .orElseThrow(() -> new StudentNotFoundException(studentId));
    }

    private List<SubjectDTO> getAllStudentGradesInList(List<Grade> grades) {
        List<SubjectDTO> listOfSubjectDTO = new ArrayList<>();
        for (GradeSubject subject : GradeSubject.values()) {
            List<Grade> subjectGrade =  grades.stream().filter(grade -> grade.getGradeSubject().equals(subject)).toList();
            listOfSubjectDTO.add(mapListGradeToSubjectDTO(subject.toString(),subjectGrade));
        }
        return listOfSubjectDTO;
    }


    private GradeSubject getGradeSubjectByOrdinal(int subjectID) {
        GradeSubject[] gradeSubjects = GradeSubject.values();
        if (subjectID < gradeSubjects.length && subjectID >= 0)
            return GradeSubject.values()[subjectID];
        else
            throw new SubjectNotFoundException(subjectID);
    }

    private SubjectDTO mapListGradeToSubjectDTO(String subject,List<Grade> grades) {
        return SubjectDTO
                .builder()
                .subject(subject)
                .gradeAverage(String.format("%.2f",getSubjectGradesAverage(grades)))
                .grades(getGradeViewDtoList(grades))
                .build();

    }

    private List<GradeViewDTO> getGradeViewDtoList(List<Grade> grades) {
        return grades
                .stream()
                .map(grade -> GradeMapper.mapGradeToGradeViewDTO(grade))
                .toList();
    }

    private double getSubjectGradesAverage(List<Grade> grades) {
        double values  = sumAllSubjectGradesValue(grades);
        if (values==0)
            return 0;
        else
            return  values/getWeighsGrades(grades);
    }

    private double sumAllSubjectGradesValue(List<Grade> grades) {
        return  grades
                .stream()
                .filter(grade -> isDegreeHaveValue(grade))
                .map(grade -> calculateGradeValue(grade))
                .mapToDouble(Double::doubleValue)
                .sum();
    }

    private double getWeighsGrades(List<Grade> grades) {
        return  grades
                .stream()
                .filter(grade -> isDegreeHaveValue(grade))
                .map(grade -> grade.getWeight())
                .mapToInt(Integer::intValue)
                .sum();
    }

    private boolean isDegreeHaveValue(Grade grade) {
        return grade.getGradeSymbolValue().getValue() != 0;
    }

    private double calculateGradeValue(Grade grade) {
        return grade.getGradeSymbolValue().getValue()*grade.getWeight();
    }

    private Grade changeGradeInformationAndSave(Grade grade, GradeDTO gradeDTO) {
            grade.setGradeSymbolValue(getDegreeBySymbol(gradeDTO.degree()));
            grade.setDescription(gradeDTO.description());
            grade.setWeight(gradeDTO.weight());
            return gradeRepository.save(grade);
    }

    private GradeSymbolValue getDegreeBySymbol(String symbol) {
        return Arrays.stream(GradeSymbolValue.values())
                .filter(degree -> degree.getSymbol().equals(symbol))
                .toList().get(0);
    }

    private Grade deleteGrade(Grade grade) {
        gradeRepository.delete(grade);
        return grade;
    }

}
