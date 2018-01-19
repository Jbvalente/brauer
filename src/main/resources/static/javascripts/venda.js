Brauer.Venda = (function (){
	
	function Venda(tabelaItens) {
		this.tabelaItens = tabelaItens;
		this.valorTotalBox = $('.js-valor-total-box');
		this.valorFreteInput = $('#valorFrete');
		this.valorDescontoInput = $('#valorDesconto');
		this.valorTotalBoxContainer = $('.js-valor-total-box-container');
		
		this.valorTotalItens = this.tabelaItens.valorTotal();
		this.valorFrete = this.valorFreteInput.data('valor');
		this.valorDesconto = this.valorDescontoInput.data('valor');
	}
	
	Venda.prototype.iniciar = function() {
		this.tabelaItens.on('tabela-itens-atualizada', onTabelaItensAtualizada.bind(this));
		this.valorFreteInput.on('keyup', onValorFreteAlterado.bind(this));
		this.valorDescontoInput.on('keyup', onValorDescontoAlterado.bind(this));
		
		this.tabelaItens.on('tabela-itens-atualizada', onValoresAlterados.bind(this));
		this.valorFreteInput.on('keyup', onValoresAlterados.bind(this));
		this.valorDescontoInput.on('keyup', onValoresAlterados.bind(this));
		
		onValoresAlterados.call(this);
	}
	
	function onTabelaItensAtualizada(evento, valorTotalItens){
		this.valorTotalItens = valorTotalItens == null ? 0 : parseFloat(valorTotalItens);
		/*console.log('valorTotalItens: ', valorTotalItens );
		console.log('Valor total itens: ', this.valorTotalItens );*/
	}
	
	function onValorFreteAlterado(evento) {
		this.valorFrete = Brauer.recuperarValor($(evento.target).val());
		/*console.log('valorFrete: ', this.valorFrete );*/
	}
	
	function onValorDescontoAlterado(evento) {
		this.valorDesconto = Brauer.recuperarValor($(evento.target).val());
		/*console.log('valorDesconto: ', this.valorDesconto );*/
	}
	
	function onValoresAlterados() {
		var valorTotal = numeral(this.valorTotalItens) + numeral(this.valorFrete) - numeral(this.valorDesconto);
		/*console.log('Valor total itens: ', this.valorTotalItens );
		console.log('valorFrete: ', this.valorFrete );
		console.log('valorDesconto: ', this.valorDesconto );
		console.log('valorTotal: ', valorTotal );*/
		this.valorTotalBox.html(Brauer.formatarMoeda(valorTotal));
		this.valorTotalBoxContainer.toggleClass('negativo', valorTotal < 0);
	}
	
	return Venda;
	
})();

$(function (){
	var autocomplete = new Brauer.Autocomplete();
	autocomplete.iniciar();
	
	var tabelaItens = new Brauer.TabelaItens(autocomplete);
	tabelaItens.iniciar();
	
	var venda = new Brauer.Venda(tabelaItens);
	venda.iniciar();
	
})