package com.github.starwacki.components.student.service;

import com.github.starwacki.global.model.account.Student;
import com.github.starwacki.global.model.account.Teacher;
import com.github.starwacki.components.student.exceptions.exception.StudentNotFoundException;
import com.github.starwacki.components.student.exceptions.exception.TeacherNotFoundException;
import com.github.starwacki.global.model.grades.Degree;
import com.github.starwacki.global.repositories.TeacherRepository;
import com.github.starwacki.components.student.dto.GradeViewDTO;
import com.github.starwacki.components.student.dto.SubjectDTO;
import com.github.starwacki.components.student.exceptions.exception.SubjectNotFoundException;
import com.github.starwacki.components.student.mapper.GradeMapper;
import com.github.starwacki.global.model.grades.Grade;
import com.github.starwacki.global.model.grades.Subject;
import com.github.starwacki.global.repositories.GradeRepository;
import com.github.starwacki.global.repositories.StudentRepository;
import com.github.starwacki.components.student.dto.GradeDTO;
import com.github.starwacki.components.student.dto.StudentGradesDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.*;
import static com.github.starwacki.components.student.mapper.StudentMapper.mapStudentToStudentGradeDTO;

@Service
@AllArgsConstructor
public class StudentGradeService {

    private final GradeRepository gradeRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;

    public StudentGradesDTO getOneSubjectGrades(int studentId, int subjectId) {
        return studentRepository
                .findById(studentId)
                .map(student -> mapStudentToStudentGradeDTO(student,getOnlyOneSubjectDTOInList(studentId,subjectId)))
                .orElseThrow(() -> new StudentNotFoundException(studentId));
    }
    public StudentGradesDTO getAllSubjectGradesByStudentID(int studentId) {
        return studentRepository
                .findById(studentId)
                .map(student -> mapStudentToStudentGradeDTO(student,getSubjectDTOList(studentId)))
                .orElseThrow(() -> new StudentNotFoundException(studentId));
    }

    public GradeViewDTO getOneGrade(int studentId, int gradeID) {
        return gradeRepository
                .findByStudentIdAndId(studentId,gradeID)
                .map(grade -> GradeMapper.mapGradeToGradeViewDTO(grade))
                .orElseThrow(() -> new StudentNotFoundException(studentId,gradeID));
    }

    public GradeDTO  addGradeToStudent(GradeDTO gradeDTO, int studentID) {
         gradeRepository
                 .save(GradeMapper
                   .mapGradeDTOToGrade(
                      gradeDTO,
                      getDegreeBySymbol(gradeDTO.degree()),
                      getStudent(studentID),
                      getTeacher(gradeDTO.addingTeacherId())));
         return gradeDTO;
    }

    public GradeDTO updateGrade(int studentID, int gradeID, GradeDTO gradeDTO) {
        return gradeRepository.findByStudentIdAndId(studentID,gradeID)
                .map(grade -> GradeMapper.mapGradeToGradeDTO(changeGradeInformationAndSave(grade,gradeDTO)))
                .orElseThrow(() -> new StudentNotFoundException(studentID));
    }

    public GradeDTO deleteStudentGrade(int studentId, int gradeID) {
        return gradeRepository
                .findByStudentIdAndId(studentId,gradeID)
                .map(grade -> GradeMapper.mapGradeToGradeDTO(deleteGrade(grade)))
                .orElseThrow(() -> new StudentNotFoundException(studentId));
    }


    private Teacher getTeacher(int addingTeacherId) {
        return teacherRepository
                .findById(addingTeacherId)
                .orElseThrow(() -> new TeacherNotFoundException(addingTeacherId));
    }

    private Student getStudent(int id) {
       return studentRepository
               .findById(id)
               .orElseThrow(()-> new StudentNotFoundException(id));
    }


    private List<SubjectDTO> getSubjectDTOList(int studentID) {
        return Arrays
                .stream(Subject.values())
                .map(subject -> getSubjectDTOForStudent(studentID,subject))
                .toList();
    }

    private List<SubjectDTO> getOnlyOneSubjectDTOInList(int studentID, int subjectID) {
        if (subjectID < Subject.values().length) {
            return List.of(getSubjectDTOForStudent(studentID, Subject.values()[subjectID]));
        } else
            throw new SubjectNotFoundException(subjectID);
    }

    private SubjectDTO getSubjectDTOForStudent(int studentID,Subject subject) {
        return SubjectDTO
                .builder()
                .grades(getSubjectGrades(studentID,subject))
                .subject(subject)
                .gradeAverage(String.format("%.2f",getSubjectGradesAverage(studentID,subject)))
                .build();
    }

    private double getSubjectGradesAverage(int studentID,Subject subject) {
        double values  = sumAllSubjectGradesValue(studentID,subject);
        if (values==0)
            return 0;
        else
            return  values/getWeighsGrades(studentID,subject);
    }

    private double sumAllSubjectGradesValue(int studentID,Subject subject) {
        return gradeRepository.findAllByStudentIdAndSubject(studentID,subject)
                .stream()
                .filter(grade -> isDegreeHaveValue(grade))
                .map(grade -> calculateGradeValue(grade))
                .mapToDouble(Double::doubleValue)
                .sum();
    }

    private double getWeighsGrades(int studentID,Subject subject) {
        return gradeRepository.findAllByStudentIdAndSubject(studentID,subject)
                .stream()
                .filter(grade -> isDegreeHaveValue(grade))
                .map(grade -> grade.getWeight())
                .mapToInt(Integer::intValue)
                .sum();
    }

    private boolean isDegreeHaveValue(Grade grade) {
        if (grade.getDegree().getValue()!=0)
            return true;
        else
            return false;
    }

    private double calculateGradeValue(Grade grade) {
        return grade.getDegree().getValue()*grade.getWeight();
    }

    private List<GradeViewDTO> getSubjectGrades(int studentID, Subject subject) {
        return gradeRepository.findAllByStudentIdAndSubject(studentID,subject)
                .stream().map(grade -> GradeMapper.mapGradeToGradeViewDTO(grade))
                .toList();
    }

    private Grade changeGradeInformationAndSave(Grade grade, GradeDTO gradeDTO) {
            grade.setDegree(getDegreeBySymbol(gradeDTO.degree()));
            grade.setDescription(gradeDTO.description());
            grade.setWeight(gradeDTO.weight());
            return gradeRepository.save(grade);
    }

    private Degree getDegreeBySymbol(String symbol) {
        return Arrays.stream(Degree.values()).filter(degree -> {
            if (degree.getSymbol().equals(symbol)) {
                return true;
            }
            else return false;
        }).toList().get(0);
    }

    private Grade deleteGrade(Grade grade) {
        gradeRepository.delete(grade);
        return grade;
    }

}
