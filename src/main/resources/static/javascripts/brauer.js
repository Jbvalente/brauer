var Brauer = Brauer || {};

Brauer.MaskMoney = (function(){
	
	function MaskMoney(){
		this.decimal = $('.js-decimal');
		this.inteiro = $('.js-inteiro');
	}
	
	MaskMoney.prototype.enable = function(){
		this.decimal.maskMoney({ decimal: ',',  thousands: '.'});
		this.inteiro.maskMoney({precision: 0, thousands: '.'});
	}
	
	return MaskMoney;
	
})();

Brauer.MascaraTelefone = ( function (){
	function MascaraTelefone (){
		this.inputTelefone = $('.js-telefone');
	}
	
	MascaraTelefone.prototype.enable = function (){
		var maskComportamento = function (val) {
			  return val.replace(/\D/g, '').length === 11 ? '(00) 00000-0000' : '(00) 0000-00009';
		}
		
		var opcoes = {
		  onKeyPress: function(val, e, field, options) {
		      field.mask(maskComportamento.apply({}, arguments), options);
		   }
		};

		this.inputTelefone.mask(maskComportamento, opcoes);
		
	}
	
	return MascaraTelefone;
	
})();

Brauer.MascaraCep= (function (){
	
	function MascaraCep (){
		this.inputCep=$('.js-mascara-cep');
	}
	
	MascaraCep.prototype.enable = function (){
		this.inputCep.mask('00.000-000');
	}
	
	return MascaraCep;
	
})();

Brauer.MascaraData = (function (){
	
	function MascaraData (){
		this.inputData = $('.js-date');
	}
	
	MascaraData.prototype.enable = function (){
		this.inputData.mask('00/00/0000');
		this.inputData.datepicker({
			orientation: 'bottom',
			language: 'pt-BR',
			autoclose: true
		});
	}
	
	return MascaraData;
	
})();

Brauer.Security = (function (){
	
	function Security (){
		this.token = $('input[name=_csrf]').val();
		this.header = $('input[name=_csrf_header]').val();
	}
	
	Security.prototype.enable = function (){
		$(document).ajaxSend(function (event, jqxhr, settings){
			jqxhr.setRequestHeader(this.header, this.token);
		}.bind(this));
	}
	
	return Security;
	
})();

numeral.language('pt-br');
Brauer.formatarMoeda = function(valor) {
	return  numeral(valor).format('0,0.00')
}

Brauer.recuperarValor = function(valorFormatado) {
	return numeral(valorFormatado).value();
}

$(function () {
	var maskMoney = new Brauer.MaskMoney();
	maskMoney.enable();
	
	var maskTelefone = new Brauer.MascaraTelefone ();
	maskTelefone.enable();
	
	var maskCep = new Brauer.MascaraCep();
	maskCep.enable();
	
	var maskData = new Brauer.MascaraData();
	maskData.enable();
	
	var security = new Brauer.Security();
	security.enable();
});