package com.jspider.mvcproject1.controller;

import java.util.List;
import java.util.jar.Attributes.Name;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.jspider.mvcproject1.pojo.StudentPOJO;
import com.jspider.mvcproject1.service.StudentService;

@Controller
public class StudentController {

	@Autowired
	private StudentService service;

	// home controller
	@GetMapping("/home")
	public String home(@SessionAttribute(name = "login", required = false) StudentPOJO login, ModelMap map) {
		if (login != null) {
			return "Home";
		}
		map.addAttribute("msg", "Login to proceed..!!");
		return "Login";

	}

	// login form controller
	@GetMapping("/login")
	public String login() {
		return "Login";
	}

	// login controller
	@PostMapping("/login")
	public String loginData(HttpServletRequest request, @RequestParam String username, @RequestParam String password,
			ModelMap map) {
		StudentPOJO student = service.login(username, password);
		if (student != null) {
			HttpSession session = request.getSession();
			session.setAttribute("login", student);
			session.setMaxInactiveInterval(10);
			return "Home";
		}
		map.addAttribute("msg", "Invalid username or password");
		return "Login";
	}

	// Add form controller
	@GetMapping("/add")
	public String add(@SessionAttribute(name = "login", required = false) StudentPOJO login, ModelMap map) {
		if (login != null) {
			return "Add";
		}
		map.addAttribute("msg", "login to proceed..!!");
		return "Login";
	}

	// add response controller
	@PostMapping("/add")
	public String addStudent(@SessionAttribute(name = "login", required = false) StudentPOJO login,
			@RequestParam String name, @RequestParam String email, @RequestParam String contact,
			@RequestParam String city, @RequestParam String username, @RequestParam String password, ModelMap map) {
		if (login != null) {
			StudentPOJO student = service.add(name, email, contact, city, username, password);
			if (student != null) {
				map.addAttribute("student", student);
				map.addAttribute("msg", "Student added successfully..!!");
			} else {
				map.addAttribute("msg", "Student not Added");
			}
			return "Add";

		}

		map.addAttribute("msg", "Student not added");
		return "Login";

	}

	// Search from controller
	@GetMapping("/search")
	public String search(@SessionAttribute(name = "login", required = false) StudentPOJO login, ModelMap map) {
		if (login != null) {
			return "Search";
		}
		map.addAttribute("msg", "Login to proceed");
		return "Login";
	}

	// Search Response Controller
	@PostMapping("/search")
	public String searchData(@SessionAttribute(name = "login", required = false) StudentPOJO login,
			@RequestParam int id, ModelMap map) {
		if (login != null) {
			StudentPOJO student = service.search(id);
			if (student != null) {
				// success
				map.addAttribute("student", student);
				return "Search";
			}
			// failure
			map.addAttribute("msg", "Student data does not exist..!!");
			return "Search";
		}
		map.addAttribute("msg", "Login to proceed");
		return "Login";
	}

	// remove from controller
	@GetMapping("/remove")
	public String remove(@SessionAttribute(name = "login", required = false) StudentPOJO login, ModelMap map) {
		if (login != null) {
			List<StudentPOJO> students = service.searchAll();
			map.addAttribute("students", students);
			return "Remove";
		}
		map.addAttribute("msg", "Login to proced..!!");
		return "Login";
	}

	// remove response controller
	@PostMapping("/remove")
	public String removeData(@SessionAttribute(name = "login", required = false) StudentPOJO login,
			@RequestParam int id, ModelMap map) {
		if (login != null) {
			StudentPOJO student = service.remove(id);
			if (student != null) {
				// success
				List<StudentPOJO> students = service.searchAll();
				map.addAttribute("students", students);
				map.addAttribute("msg", "Student Remove successfully..!!");
				return "Remove";
			}
			// failer
			List<StudentPOJO> students = service.searchAll();
			map.addAttribute("students", students);
			map.addAttribute("msg", "Student Data does not exist..!!");
			return "Remove";
		}
		map.addAttribute("msg", "Login to Proceed..!!");
		return "Login";
	}

	// update page controller
	@GetMapping("/update")
	public String update(@SessionAttribute(name = "login", required = false) StudentPOJO login, ModelMap map) {
		if (login != null) {
			List<StudentPOJO> students = service.searchAll();
			map.addAttribute("students", students);
			return "Update";

		}
		map.addAttribute("msg", "login to proceed..!!");
		return "Login";

	}

	// update form controller
	@PostMapping("/update")
	public String updateForm(@SessionAttribute(name = "login", required = false) StudentPOJO login,
			@RequestParam int id, ModelMap map) {
		if (login != null) {
			StudentPOJO student = service.search(id);
			if (student != null) {
				// success
				map.addAttribute("student", student);
			}
			// failure
			List<StudentPOJO> students = service.searchAll();
			map.addAttribute("students", students);
			map.addAttribute("msg", "student does not exist");
			return "Update";
		}
		map.addAttribute("msg", "Login to proceed");
		return "Login";
	}

	// update data controller
	@PostMapping("/updateData")
	public String updateData(@SessionAttribute(name = "login", required = false) StudentPOJO login,
			@RequestParam int id, @RequestParam String name, @RequestParam String email, @RequestParam String contact,
			@RequestParam String city, @RequestParam String username, @RequestParam String password, ModelMap map) {

		if (login != null) {
			StudentPOJO student = service.update(id, name, email, contact, city, username, password);
			if (student != null) {
				// success
				map.addAttribute("msg", "student data update successfuly..!!");
				List<StudentPOJO> students = service.searchAll();
				map.addAttribute("students", students);
				return "Update";
			}
			// failuer
			map.addAttribute("msg", "Student not updated..!!");
			List<StudentPOJO> students = service.searchAll();
			map.addAttribute("students", students);
			return "Update";

		}
		map.addAttribute("msg", "Login to proceed");
		return "Login";

	}

	// logout Controller
	@GetMapping("/logout")
	public String logout(HttpSession session, ModelMap map) {
		session.invalidate();
		map.addAttribute("msg", "Logged out successfully..!!");
		return "Login";
	}
}
