package com.springboot.employees.job;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.springboot.employees.email.EmailService;

@Component
@EnableScheduling
@EnableAsync
public class SchedulerJob {
	
	@Autowired
	private EmailService emailService;
	
//	@Async
//	@Scheduled(cron = "0/5 * * * * *")
//	public void testSchedulerJob() {
//		System.out.println("Hii...test1 SchedulerJob... for 2 min"+new Date());
//	}
	
//	@Async
//	@Scheduled(cron = "0/5 * * * * *")
//	public void testSchedulerJob2() {
//		System.out.println("Hii...test2 SchedulerJob..."+new Date());
//		emailService1.sendSimpleEmail("skch2710@gmail.com", "this is text from sample mail."+new Date(), "");
//	}
	
//	@Scheduled(cron = "0 */2 * ? * *")
//	public void testSendMailSchedulerJob() {
//		System.out.println("Hii...test2 SchedulerJob..."+new Date());
//		
//		Map<String, Object> model = new HashMap<>();
//		
//		model.put("firstName", "hari");
//		model.put("lastName", "haran");
//		model.put("link", "www.google.com");
//		
//		emailService.sendEmailCheck(model, "hariharankunta@gmail.com");
//	}

}
