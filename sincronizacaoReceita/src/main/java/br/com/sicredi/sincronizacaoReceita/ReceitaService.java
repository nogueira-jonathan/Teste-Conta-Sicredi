package br.com.sicredi.sincronizacaoReceita;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class ReceitaService {

	public boolean atualizarConta(String agencia, String conta, double saldo, String status)
			throws RuntimeException, InterruptedException {

		if (agencia == null || agencia.length() != 4) {
			return false;
		}

		if (conta == null || conta.length() != 6) {
			return false;
		}

		List tipos = new ArrayList();
		tipos.add("A");
		tipos.add("I");
		tipos.add("B");
		tipos.add("P");

		if (status == null || !tipos.contains(status)) {
			return false;
		}

		long wait = Math.round(Math.random() * 4000) + 1000;
		Thread.sleep(wait);

		long randomError = Math.round(Math.random() * 1000);
		if (randomError == 500) {
			throw new RuntimeException("Error");
		}

		return true;
	}
}
