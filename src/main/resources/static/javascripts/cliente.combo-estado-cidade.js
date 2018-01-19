var Brauer = Brauer || {};

Brauer.ComboEstado = (function (){
	
	function ComboEstado(){
		this.combo = $('#estado');
		this.emitter = $({});
		this.on = this.emitter.on.bind(this.emitter);	
	}
	
	ComboEstado.prototype.iniciar = function (){
		this.combo.on('change', onEstadoAlterado.bind(this));
	}
	
	function onEstadoAlterado (){
		/*console.log('estado selecionado', this.combo.val());*/
		this.emitter.trigger('alterado', this.combo.val());
	}
	
	return ComboEstado;
	
})();

Brauer.ComboCidade= (function (){
	
	function ComboCidade (comboEstado){
		this.comboEstado = comboEstado;
		this.combo = $('#cidade');
		this.imgLoading = $('.js-img-loading');
		this.inputHiddenCidadeSelecionada = $('#inputHiddenCidadeSelecionada');
	}
	
	ComboCidade.prototype.iniciar = function (){
		var codigoEstado = this.comboEstado.combo.val();
		
		reset.call(this);
		this.comboEstado.on('alterado', onEstadoAlterado.bind(this))
		
		/*console.log('codigo estado: ', codigoEstado);*/
		inicializarCidades.call(this, codigoEstado );
	}
	
	function onEstadoAlterado (evento, codigoEstado) {
		
		this.inputHiddenCidadeSelecionada.val('');
		inicializarCidades.call(this, codigoEstado );
		
		/*console.log('codigo do estado no combo cidade: ', codigoEstado);*/
	}
	
	function inicializarCidades (codigoEstado){
		if (codigoEstado){
			var resposta = $.ajax({
				url: this.combo.data('url'),
				method: 'GET',
				contentType: 'application/json',
				data: { 'estado': codigoEstado},
				beforeSend: iniciarRequisicao.bind(this),
				complete: finalizarRequisicao.bind(this)
			});
			resposta.done(onBuscarCidadesFinalizado.bind(this));
		} else {
			reset.call(this);
		}
	}
	
	function onBuscarCidadesFinalizado(cidades){
		var options = [];
		var codigoCidadeSelecionada = this.inputHiddenCidadeSelecionada.val();
		cidades.forEach (function (cidade){
			options.push('<option value="' + cidade.codigo + '">' + cidade.nome + '</option>' );
		});
		
		this.combo.html(options.join(''));
		this.combo.removeAttr('disabled');
		
		if (codigoCidadeSelecionada){
			this.combo.val(codigoCidadeSelecionada);
		}
	}
	
	function reset (){
		this.combo.html('<option value="">Selecione a cidade</option>');
		this.combo.val('');
		this.combo.attr('disabled', 'disabled');
	}
	
	function iniciarRequisicao (){
		reset.call(this);
		this.imgLoading.show();
	}
	
	function finalizarRequisicao(){
		this.imgLoading.hide();
	}
	
	return ComboCidade;
	
})();



$(function (){
	var comboEstado = new Brauer.ComboEstado();
	comboEstado.iniciar();
	
	var comboCidade = new Brauer.ComboCidade(comboEstado);
	comboCidade.iniciar();
});