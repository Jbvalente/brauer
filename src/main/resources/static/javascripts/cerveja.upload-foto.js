var Brauer = Brauer || {};

Brauer.UploadFoto = (function (){
	
	function UploadFoto (){
		this.inputNomeFoto = $('input[name=foto]');
		this.inputTipoArquivo = $('input[name=tipoArquivo]');
		this.novaFoto = $('input[name=novaFoto]');
		
		this.htmlFotoCervejaTemplate = $('#foto-cerveja').html();
		this.template = Handlebars.compile(this.htmlFotoCervejaTemplate);
		
		this.uploadDrop = $('#upload-drop');
		this.containerFotoCerveja = $('.js-container-foto-cerveja');
		
		
	}
	
	UploadFoto.prototype.iniciar = function (){
		var settings = {
				type: 'json',
				filelimit: 1,
				allow: '*.(jpg|jpeg|png)',
				action: this.containerFotoCerveja.data('url-fotos'),
				complete: onUploadCompleto.bind(this),
				beforeSend: adicionarCsrfToken
		}
		
		UIkit.uploadSelect($('#upload-select'), settings);
		UIkit.uploadDrop(this.uploadDrop, settings);
		
		if (this.inputNomeFoto.val()){
			renderizarFoto.call(this, {nome: this.inputNomeFoto.val(), tipoArquivo: this.inputTipoArquivo.val()})
		}
	}
	
	function onUploadCompleto(resposta){
		this.novaFoto.val('true');
		renderizarFoto.call(this, resposta);
	}
	
	function renderizarFoto(resposta){
		this.inputNomeFoto.val(resposta.nome);
		this.inputTipoArquivo.val(resposta.tipoArquivo);
		
		this.uploadDrop.addClass('hidden');
		
		var foto = '';
		if(this.novaFoto.val() == 'true'){
			foto = 'temp/';
		}
		foto += resposta.nome;
		
		var htmlFotoCerveja = this.template({foto: foto});
		this.containerFotoCerveja.append(htmlFotoCerveja);
		
		$('.js-remove-foto').on('click', onRemoverFoto.bind(this));
	}
	
	function onRemoverFoto (){
		$('.js-foto-cerveja').remove();
		this.uploadDrop.removeClass('hidden');
		this.inputNomeFoto.val('');
		this.inputTipoArquivo.val('');
		this.novaFoto.val('false');
	}
	
	function adicionarCsrfToken (xhr){
		var token = $('input[name=_csrf]').val();
		var header = $('input[name=_csrf_header]').val();
		xhr.setRequestHeader(header, token);
	}
	
	return UploadFoto;
	
})();

$(function (){
	var uploadFoto = new Brauer.UploadFoto();
	uploadFoto.iniciar();
});