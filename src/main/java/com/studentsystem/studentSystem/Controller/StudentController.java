package com.studentsystem.studentSystem.Controller;

import com.studentsystem.studentSystem.Entity.PageData;
import com.studentsystem.studentSystem.Entity.Student;
import com.studentsystem.studentSystem.Entity.User;
import com.studentsystem.studentSystem.Repository.StudentRepository;
import com.studentsystem.studentSystem.Repository.UserRepository;
import com.studentsystem.studentSystem.Service.StorageService;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.Optional;

@Controller
public class StudentController {
    @Autowired
    private ServletContext servletContext;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private UserRepository userRepository;
	private final StorageService storageService;
    @Autowired
    public StudentController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping(value = { "/addstudent/{studentId}", "/addstudent" })
    public String addStudent(@PathVariable(value = "studentId", required = false) Integer studentId, Model model) {
        if (studentId != null) {
            Optional<Student> student = studentRepository.findById(studentId);
            if (student.isPresent()) {
                model.addAttribute("student", student.get());
            }

        }
        model.addAttribute("pageTitle", "Student Management System|Add Student");
        return "addStudentForm";
    }
    @GetMapping(value={"/","home"})
    public String addStudent(Model model) {

        model.addAttribute("pageTitle", "Student Management System|Home");
        return "home";
    }
    @PostMapping("/savestudent")
    public String saveStudent(
            @ModelAttribute Student student, RedirectAttributes redirectAttributes,@RequestParam("photo") MultipartFile photo) {

         String name= storageService.store(photo);
         student.setStudentPhoto(name);
        studentRepository.save(student);
        redirectAttributes.addFlashAttribute("Status", "success");
        String message = student.getStudentId() != null ? "Updated" : "Added";
        redirectAttributes.addFlashAttribute("Message", "Student " + message + " Successfully");
        return "redirect:/getallstudents";
    }

    @GetMapping(value = { "/getallstudents"})
    public String getAllStudents(Model model, @RequestParam(required = false) String query,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String data,
            @RequestParam(required = false) Integer currentPage) {
        if (currentPage == null) {
            currentPage = 0;
        }
        Pageable paging = PageRequest.of(currentPage, 5);
        Page<Student> pagedResult = null;
        PageData pageData = new PageData();

        if (query != null && !query.equals("")) {
            pagedResult = studentRepository.findStudentByStudentNameContainingIgnoreCase(query, paging);

            // model.addAttribute("Students",
            // studentRepository.findStudentByStudentNameContainingIgnoreCase(query,paging));

        } else if (type != null && data != null && !type.equals("") && !data.equals("")) {
            switch (type) {
                case "studentDepartment" ->
                    pagedResult = studentRepository.findStudentByStudentDepartmentContainingIgnoreCase(data, paging);
                case "studentClass" ->
                    pagedResult = studentRepository.findStudentByStudentClassContainingIgnoreCase(data, paging);
                case "studentGender" ->
                    pagedResult = studentRepository.findStudentByStudentGenderContainingIgnoreCase(data, paging);
            }

            model.addAttribute("type", type);
            model.addAttribute("data", data);
        
            
        } else {
            pagedResult = studentRepository.findAll(paging);
        }
        assert pagedResult != null;
    
       
       pageData.setNoOfPages(pagedResult.getTotalPages());
        pageData.setCurrentPage(currentPage);
        pageData.setHasNext(pagedResult.hasNext());
        pageData.setHasPrevious(pagedResult.hasPrevious());
        model.addAttribute("Students", pagedResult);
        model.addAttribute("pageData", pageData);
        model.addAttribute("pageTitle", "Student Management System|View Student");
        return "ViewStudent";
    }

    @GetMapping("/deletestudent/{id}")
    public String deleteStudent(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        Optional<Student> optional2 = studentRepository.findById(id);
        optional2.orElseThrow(() -> new IllegalArgumentException("Invalid student id:" + id));
        studentRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("Status", "success");

        redirectAttributes.addFlashAttribute("Message", "Student deleted Successfully");

        return "redirect:/getallstudents";
    }

    @PostMapping(value = { "/viewsinglestudent/{studentId}" })
    public @ResponseBody Optional<Student> viewSingleStudent(
            @PathVariable(value = "studentId", required = false) Integer studentId) {
        Optional<Student> student = Optional.empty();
        if (studentId != null) {
            student = studentRepository.findById(studentId);

        }
        return student;
    }

    @GetMapping("/getStudents")
    public String getStudents(Model model) {

        model.addAttribute("students", studentRepository.findAll());
        return "studentList";
    }
    @GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) {
        Resource resource = null;
        // try {
            resource = storageService.loadFile(fileName);
        // } catch (IOException e) {
        //     return ResponseEntity.notFound().build();
        // }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(MediaType.APPLICATION_OCTET_STREAM_VALUE))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
    @GetMapping("/login")
    public String login(Model model) {

        return "userlogin";
    }
    @PostMapping("/checklogin")
    public String checkLogin(Model model,@ModelAttribute User user,HttpSession session){
        Optional<User> user1=userRepository.findByUserEmailAndUserPassword(user.getUserEmail(),user.getUserPassword());
        if (user1.isPresent()) {
            session.setAttribute("userEmail", user.getUserEmail());
            return "redirect:/getallstudents";
        }else{
            return "redirect:/login";
        }
        
    }
   
public boolean checkSessionKeyExists(HttpSession session) {
    String keyToCheck = "userEmail";
    Object value = session.getAttribute(keyToCheck);
    if (value != null) {
        return true;
    } else {
        return false;
    }
}
@GetMapping("/logout")
public String logout(HttpSession session) {
    session.removeAttribute("userEmail");
    return "redirect:/login";

}

}
