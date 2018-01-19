package com.tjgo.brauer.thymeleaf.processor;

import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.IModel;
import org.thymeleaf.model.IModelFactory;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractElementTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

public class MensagemElementTagProcessor extends AbstractElementTagProcessor {

	private static final String NOME_TAG = "mensagem";
	private static final int PRECEDENCIA = 1000;
	
	public MensagemElementTagProcessor(String dialetoPrefixo) {
		super(TemplateMode.HTML, dialetoPrefixo, NOME_TAG, true, null, false, PRECEDENCIA);
	}

	@Override
	protected void doProcess(ITemplateContext context, IProcessableElementTag tag, IElementTagStructureHandler structureHandler) {
		IModelFactory modelFactory = context.getModelFactory();
		IModel model = modelFactory.createModel();
		
		model.add(modelFactory.createStandaloneElementTag("th:block", "th:replace", "fragmentos/MensagemSucesso :: layout-sucesso"));
		model.add(modelFactory.createStandaloneElementTag("th:block", "th:replace", "fragmentos/MensagensErroValidacao :: layout-erro-validacao"));
		
		structureHandler.replaceWith(model, true);
	}

}
