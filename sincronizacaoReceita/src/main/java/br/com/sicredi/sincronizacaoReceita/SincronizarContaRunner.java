package br.com.sicredi.sincronizacaoReceita;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

public class SincronizarContaRunner implements ApplicationRunner {

	public static final String ERRO_NAME_FILE = "Erro no nome do arquivo";
	private static final String ERRO_AO_CRIAR_ARQUIVO = "Erro ao criar novo arquivo";
	public static final String ARQUIVO_EXISTENTE = "Arquivo já existente";
	private final SincronizarReceitaServiceImp sincronizarReceitaServiceImp;
	private final Logger log;

	public SincronizarContaRunner(SincronizarReceitaServiceImp sincronizarReceitaServiceImp) {
		this.sincronizarReceitaServiceImp = sincronizarReceitaServiceImp;
		this.log = LoggerFactory.getLogger(SincronizarContaRunner.class);
	}

	@Override
	public void run(ApplicationArguments args) {
		try {
			File file = getFileFromArgs(args);
			File targetFile = createTargetFile(file);

			sincronizarReceitaServiceImp.processarArquivos(file, targetFile);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	private File getFileFromArgs(ApplicationArguments args) throws Exception {
		String filename;
		if (!args.getNonOptionArgs().isEmpty()) {
			filename = args.getNonOptionArgs().get(0);
		} else {
			throw new BusinessException(ERRO_NAME_FILE);
		}
		File file = new File(filename);
		if (!file.isFile())
			throw new FileNotFoundException();

		return file;
	}

	private File createTargetFile(File file) {
		File targetFile = new File(file.getAbsolutePath().replace(".csv", "").concat("_sincronizado.csv"));
		try {
			boolean fileCreated = targetFile.createNewFile();
			if (!fileCreated)
				log.warn(ARQUIVO_EXISTENTE);

			return targetFile;
		} catch (IOException e) {
			throw new BusinessException(ERRO_AO_CRIAR_ARQUIVO);
		}
	}

}
