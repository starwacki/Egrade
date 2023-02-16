package com.github.starwacki.student.service;

import com.github.starwacki.account.model.Student;
import com.github.starwacki.student.exceptions.StudentNotFoundException;
import com.github.starwacki.student.mapper.GradeMapper;
import com.github.starwacki.student.model.Grade;
import com.github.starwacki.student.model.Subject;
import com.github.starwacki.repositories.GradeRepository;
import com.github.starwacki.repositories.StudentRepository;
import com.github.starwacki.student.dto.GradeDTO;
import com.github.starwacki.student.dto.StudentGradesDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import static com.github.starwacki.student.mapper.StudentMapper.mapStudentToStudentGradeDTO;

@Service
@AllArgsConstructor
public class StudentGradeService {

    private final GradeRepository gradeRepository;
    private final StudentRepository studentRepository;


    public StudentGradesDTO getOneSubjectGrade(int studentId, int subjectId) {
        return studentRepository
                .findById(studentId)
                .map(student -> mapStudentToStudentGradeDTO(student,getGrades(studentId,subjectId)))
                .orElseThrow(() -> new StudentNotFoundException());
    }
    public StudentGradesDTO getAllStudentsGrade(int studentId) {
        return studentRepository
                .findById(studentId)
                .map(student -> mapStudentToStudentGradeDTO(student,getGrades(studentId)))
                .orElseThrow(() -> new StudentNotFoundException());
    }

    public GradeDTO getOneGrade(int id, int gradeID) {
        return gradeRepository
                .findByStudentIdAndId(id,gradeID)
                .map(grade -> GradeMapper.mapGradeToGradeViewDTO(grade))
                .orElseThrow(() -> new StudentNotFoundException());
    }

    public GradeDTO  addGradeToStudent(GradeDTO gradeDTO, int studentID) {
         gradeRepository.save(GradeMapper.mapGradeDTOToGrade(gradeDTO,getStudent(studentID)));
         return gradeDTO;
    }

    private Student getStudent(int id) {
       return studentRepository
               .findById(id)
               .orElseThrow(()-> new StudentNotFoundException());
    }

    private Map<Subject, List<GradeDTO>> getGrades(int studentID) {
        Map<Subject,List<GradeDTO>> grades = new HashMap<>();
        addGradesToMap(grades,studentID);
        return grades;
    }

    private Map<Subject, List<GradeDTO>> getGrades(int studentID, int subjectID) {
        Map<Subject,List<GradeDTO>> grades = new HashMap<>();
        addSubjectGradesToMap(grades,studentID,Subject.values()[subjectID]);
        return grades;
    }

    private void addGradesToMap(Map<Subject,List<GradeDTO>> grades, int studentID) {
        Arrays
                .stream(Subject.values())
                .toList()
                .forEach(subject -> addSubjectGradesToMap(grades,studentID,subject));
    }

    private void addSubjectGradesToMap(Map<Subject,List<GradeDTO>> grades, int studentID, Subject subject) {
        grades.put(subject,mapToGradeViewDtoList(getGradeList(studentID,subject)));
    }

    private List<Grade> getGradeList(int studentID, Subject subject) {
        return gradeRepository.findAllByStudentIdAndSubject(studentID,subject);
    }

    private List<GradeDTO> mapToGradeViewDtoList(List<Grade> grades) {
        return grades
                .stream()
                .map(grade -> GradeMapper.mapGradeToGradeViewDTO(grade))
                .collect(Collectors.toList());
    }

    public GradeDTO updateGrade(int studentID, int gradeID, GradeDTO gradeDTO) {
        return gradeRepository.findByStudentIdAndId(studentID,gradeID)
                .map(grade -> GradeMapper.mapGradeToGradeViewDTO(changeGradeInformationAndSave(grade,gradeDTO)))
                .orElseThrow(() -> new StudentNotFoundException());
    }

    private Grade changeGradeInformationAndSave(Grade grade, GradeDTO gradeDTO) {
            grade.setDegree(gradeDTO.degree());
            grade.setDescription(gradeDTO.description());
            grade.setWeight(gradeDTO.weight());
            return gradeRepository.save(grade);
    }

}
