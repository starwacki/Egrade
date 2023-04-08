package com.github.starwacki.components.grades;
import com.github.starwacki.components.grades.dto.GradeRequestDTO;
import com.github.starwacki.components.grades.dto.GradeResponeDTO;
import com.github.starwacki.components.grades.dto.SubjectResponseDTO;
import com.github.starwacki.components.grades.exceptions.GradeStudentNotFoundException;
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


    SubjectResponseDTO getOneSubjectGrades(int studentId, int subjectId) {
        return gradeRepository
                .findAllByStudentIDAndGradeSubject(studentId,getGradeSubjectByOrdinal(subjectId))
                .map(grades -> mapListGradeToSubjectDTO(getGradeSubjectByOrdinal(subjectId).toString(),grades))
                .orElseThrow(() -> new GradeStudentNotFoundException(studentId));
    }

    List<SubjectResponseDTO> getAllGradesByStudentID(int studentId) {
        return   gradeRepository.findAllByStudentID(studentId)
                .map(grades -> getAllStudentGradesInList(grades))
                .orElseThrow(() -> new GradeStudentNotFoundException(studentId));
    }

     GradeResponeDTO getOneGrade(int studentId, int gradeID) {
        return gradeRepository
                .findByStudentIDAndId(studentId,gradeID)
                .map(grade -> GradeMapper.mapGradeToGradeViewDTO(grade))
                .orElseThrow(() -> new GradeStudentNotFoundException(studentId));
    }

    GradeRequestDTO addGradeToStudent(GradeRequestDTO gradeRequestDTO) {
         gradeRepository.save(GradeMapper
                   .mapGradeDTOToGrade(
                           gradeRequestDTO,
                      getDegreeBySymbol(gradeRequestDTO.degree())));
         return gradeRequestDTO;
    }

     GradeRequestDTO updateGrade(int studentID, int gradeID, GradeRequestDTO gradeRequestDTO) {
        return gradeRepository.findByStudentIDAndId(studentID,gradeID)
                .map(grade -> GradeMapper.mapGradeToGradeDTO(changeGradeInformationAndSave(grade, gradeRequestDTO)))
                .orElseThrow(() -> new GradeStudentNotFoundException(studentID));
    }

    GradeRequestDTO deleteStudentGrade(int studentId, int gradeID) {
        return gradeRepository
                .findByStudentIDAndId(studentId,gradeID)
                .map(grade -> GradeMapper.mapGradeToGradeDTO(deleteGrade(grade)))
                .orElseThrow(() -> new GradeStudentNotFoundException(studentId));
    }

    private List<SubjectResponseDTO> getAllStudentGradesInList(List<Grade> grades) {
        List<SubjectResponseDTO> listOfSubjectResponseDTO = new ArrayList<>();
        for (GradeSubject subject : GradeSubject.values()) {
            List<Grade> subjectGrade =  grades.stream().filter(grade -> grade.getGradeSubject().equals(subject)).toList();
            listOfSubjectResponseDTO.add(mapListGradeToSubjectDTO(subject.toString(),subjectGrade));
        }
        return listOfSubjectResponseDTO;
    }


    private GradeSubject getGradeSubjectByOrdinal(int subjectID) {
        GradeSubject[] gradeSubjects = GradeSubject.values();
        if (subjectID < gradeSubjects.length && subjectID >= 0)
            return GradeSubject.values()[subjectID];
        else
            throw new SubjectNotFoundException(subjectID);
    }

    private SubjectResponseDTO mapListGradeToSubjectDTO(String subject, List<Grade> grades) {
        return SubjectResponseDTO
                .builder()
                .subject(subject)
                .gradeAverage(String.format("%.2f",getSubjectGradesAverage(grades)))
                .grades(getGradeViewDtoList(grades))
                .build();

    }

    private List<GradeResponeDTO> getGradeViewDtoList(List<Grade> grades) {
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

    private Grade changeGradeInformationAndSave(Grade grade, GradeRequestDTO gradeRequestDTO) {
            grade.setGradeSymbolValue(getDegreeBySymbol(gradeRequestDTO.degree()));
            grade.setDescription(gradeRequestDTO.description());
            grade.setWeight(gradeRequestDTO.weight());
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
