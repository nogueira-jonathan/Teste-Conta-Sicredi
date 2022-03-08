package br.com.sicredi.sincronizacaoReceita;

import java.io.File;

public interface SincronizarReceitaInt {
	void processarArquivos(File arquivoCsv, File arquivoProcessado) throws Exception;
}
