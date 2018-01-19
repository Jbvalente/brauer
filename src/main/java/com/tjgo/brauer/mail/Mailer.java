package com.tjgo.brauer.mail;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.tjgo.brauer.model.Cerveja;
import com.tjgo.brauer.model.ItemVenda;
import com.tjgo.brauer.model.Venda;
import com.tjgo.brauer.storage.FotoStorage;

@Component
public class Mailer {
	
	private static Logger logger = LoggerFactory.getLogger(Mailer.class);
	
	@Autowired
	private JavaMailSender mailSender;
	
	/*@Autowired
	private Environment env;*/
	
	@Autowired
	private TemplateEngine thymeleaf;
	
	@Autowired
	private FotoStorage fotoStorage;
	
	@Async
	public void enviar(Venda venda){
		/*System.out.println(">>>> Enviando e-mail!");
		
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println(">>>> E-mail enviado!");*/
		
		/*SimpleMailMessage mensagem = new SimpleMailMessage();
		mensagem.setFrom(env.getProperty("email.Default.SMTP.Login"));
		mensagem.setTo(venda.getCliente().getEmail());
		mensagem.setSubject("Venda Efetuada");
		mensagem.setText("Obrigado, sua venda foi processada!");
		
		mailSender.send(mensagem);*/
		
		Context context = new Context(new Locale("pt", "BR"));
		context.setVariable("venda", venda);
		context.setVariable("logo", "logo");
		
		Map<String, String>fotos = new HashMap<>();
		boolean adicionarMockCerveja = false;
		for(ItemVenda item : venda.getItens()){
			Cerveja cerveja = item.getCerveja();
			if (cerveja.temFoto()){
				String cid = "foto-" + cerveja.getCodigo();
				context.setVariable(cid, cid);
				
				fotos.put(cid, cerveja.getFoto() + "|" + cerveja.getTipoArquivo());
			} else {
				adicionarMockCerveja = true;
				context.setVariable("mockCerveja", "mockCerveja");
			}
		}
		
		try {
			
			String email = thymeleaf.process("mail/ResumoVenda", context);
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
			/*mensagem.setFrom(env.getProperty("email.Default.SMTP.Login"));*/
			helper.setFrom("postmaster@sandbox5ae9e0d928b4400c89c52e222f1403bf.mailgun.org");
			helper.setTo(venda.getCliente().getEmail());
			helper.setSubject(String.format("Brauer - Venda nº %d em andamento!", venda.getCodigo()));
			helper.setText(email, true);
			
			helper.addInline("logo", new ClassPathResource("static/imagens/logo-gray-2.png"));
			
			if(adicionarMockCerveja){
				helper.addInline("mockCerveja", new ClassPathResource("static/imagens/cerveja-mock.png"));
			}
			
			for (String cid : fotos.keySet()){
				String[] fotoTipoArquivo = fotos.get(cid).split("\\|");
				String fotoCerveja = fotoTipoArquivo[0];
				String tipoArquivo = fotoTipoArquivo[1];
				byte[] arrayFoto = fotoStorage.recuperarThumbnail(fotoCerveja);
				helper.addInline(cid, new ByteArrayResource(arrayFoto), tipoArquivo);
			}
			
			mailSender.send(mimeMessage);
		} catch (MessagingException e) {
			logger.error("Erro enviando e-mail!", e);
		}
	}
}
