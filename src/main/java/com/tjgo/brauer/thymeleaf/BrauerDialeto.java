package com.tjgo.brauer.thymeleaf;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;
import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.standard.StandardDialect;

import com.tjgo.brauer.thymeleaf.processor.ClasseParaErrosAttributeTagProcessor;
import com.tjgo.brauer.thymeleaf.processor.MensagemElementTagProcessor;
import com.tjgo.brauer.thymeleaf.processor.MenuAttributeTagProcessor;
import com.tjgo.brauer.thymeleaf.processor.OrderElementTagProcessor;
import com.tjgo.brauer.thymeleaf.processor.PaginationElementTagProcessor;

@Component
public class BrauerDialeto extends AbstractProcessorDialect {

	public BrauerDialeto() {
		super("Brauer", "brauer", StandardDialect.PROCESSOR_PRECEDENCE);
		
	}

	@Override
	public Set<IProcessor> getProcessors(String dialetoPrefixo) {
		final Set<IProcessor> processadores = new HashSet<>();
		processadores.add(new ClasseParaErrosAttributeTagProcessor(dialetoPrefixo));
		processadores.add(new MensagemElementTagProcessor(dialetoPrefixo));
		processadores.add(new OrderElementTagProcessor(dialetoPrefixo));
		processadores.add(new PaginationElementTagProcessor(dialetoPrefixo));
		processadores.add(new MenuAttributeTagProcessor(dialetoPrefixo));
		return processadores;
	}

}
