package br.com.sicredi.sincronizacaoReceita;

import enums.ResultEnum;
import enums.StatusContaEnum;

public class Conta {
	private String agencia;
    private String conta;
    private Double saldo;
    private StatusContaEnum status;
    private ResultEnum processada;
    
	public Conta(String agencia, String conta, Double saldo, StatusContaEnum status) {
		this.agencia = agencia;
		this.conta = conta;
		this.saldo = saldo;
		this.status = status;
	}

	public String getAgencia() {
		return agencia;
	}

	public void setAgencia(String agencia) {
		this.agencia = agencia;
	}

	public String getConta() {
		return conta;
	}

	public void setConta(String conta) {
		this.conta = conta;
	}

	public Double getSaldo() {
		return saldo;
	}

	public void setSaldo(Double saldo) {
		this.saldo = saldo;
	}

	public StatusContaEnum getStatus() {
		return status;
	}

	public void setStatus(StatusContaEnum status) {
		this.status = status;
	}

	public ResultEnum getprocessada() {
		return processada;
	}

	public void setprocessada(ResultEnum processada) {
		this.processada = processada;
	}
	
	
    

}
