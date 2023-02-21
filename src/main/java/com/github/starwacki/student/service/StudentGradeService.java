package com.github.starwacki.student.service;

import com.github.starwacki.account.model.Student;
import com.github.starwacki.account.model.Teacher;
import com.github.starwacki.repositories.TeacherRepository;
import com.github.starwacki.student.dto.GradeViewDTO;
import com.github.starwacki.student.dto.SubjectDTO;
import com.github.starwacki.student.exceptions.StudentNotFoundException;
import com.github.starwacki.student.exceptions.TeacherNotFoundException;
import com.github.starwacki.student.mapper.GradeMapper;
import com.github.starwacki.student.model.Grade;
import com.github.starwacki.student.model.Subject;
import com.github.starwacki.repositories.GradeRepository;
import com.github.starwacki.repositories.StudentRepository;
import com.github.starwacki.student.dto.GradeDTO;
import com.github.starwacki.student.dto.StudentGradesDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.*;
import static com.github.starwacki.student.mapper.StudentMapper.mapStudentToStudentGradeDTO;

@Service
@AllArgsConstructor
public class StudentGradeService {

    private final GradeRepository gradeRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;

    public StudentGradesDTO getOneSubjectGrade(int studentId, int subjectId) {
        return studentRepository
                .findById(studentId)
                .map(student -> mapStudentToStudentGradeDTO(student,getOnlyOneSubjectDTOInList(studentId,subjectId)))
                .orElseThrow(() -> new StudentNotFoundException(studentId));
    }
    public StudentGradesDTO getAllStudentsGrade(int studentId) {
        return studentRepository
                .findById(studentId)
                .map(student -> mapStudentToStudentGradeDTO(student,getSubjectDTOList(studentId)))
                .orElseThrow(() -> new StudentNotFoundException(studentId));
    }

    public GradeViewDTO getOneGrade(int studentId, int gradeID) {
        return gradeRepository
                .findByStudentIdAndId(studentId,gradeID)
                .map(grade -> GradeMapper.mapGradeToGradeViewDTO(grade))
                .orElseThrow(() -> new StudentNotFoundException(studentId));
    }

    public GradeDTO  addGradeToStudent(GradeDTO gradeDTO, int studentID) {
         gradeRepository
                 .save(GradeMapper
                   .mapGradeDTOToGrade(
                      gradeDTO,
                      getStudent(studentID),
                      getTeacher(gradeDTO.addingTeacherId())));
         return gradeDTO;
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
        return  List.of(getSubjectDTOForStudent(studentID,Subject.values()[subjectID]));
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
                .map(grade -> calculateGradeValue(grade))
                .mapToDouble(Double::doubleValue)
                .sum();
    }

    private double getWeighsGrades(int studentID,Subject subject) {
        return gradeRepository.findAllByStudentIdAndSubject(studentID,subject)
                .stream()
                .map(grade -> grade.getWeight())
                .mapToInt(Integer::intValue)
                .sum();
    }

    private double calculateGradeValue(Grade grade) {
        return grade.getDegree()*grade.getWeight();
    }

    private List<GradeViewDTO> getSubjectGrades(int studentID, Subject subject) {
        return gradeRepository.findAllByStudentIdAndSubject(studentID,subject)
                .stream().map(grade -> GradeMapper.mapGradeToGradeViewDTO(grade))
                .toList();
    }


    public GradeDTO updateGrade(int studentID, int gradeID, GradeDTO gradeDTO) {
        return gradeRepository.findByStudentIdAndId(studentID,gradeID)
                .map(grade -> GradeMapper.mapGradeToGradeDTO(changeGradeInformationAndSave(grade,gradeDTO)))
                .orElseThrow(() -> new StudentNotFoundException(studentID));
    }

    private Grade changeGradeInformationAndSave(Grade grade, GradeDTO gradeDTO) {
            grade.setDegree(gradeDTO.degree());
            grade.setDescription(gradeDTO.description());
            grade.setWeight(gradeDTO.weight());
            return gradeRepository.save(grade);
    }

    public GradeDTO deleteStudentGrade(int studentId, int gradeID) {
        return gradeRepository
                .findByStudentIdAndId(studentId,gradeID)
                .map(grade -> GradeMapper.mapGradeToGradeDTO(deleteGrade(grade)))
                .orElseThrow(() -> new StudentNotFoundException(studentId));
    }

    private Grade deleteGrade(Grade grade) {
        gradeRepository.delete(grade);
        return grade;
    }

}
