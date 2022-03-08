package br.com.sicredi.sincronizacaoReceita;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import enums.ResultEnum;
import enums.StatusContaEnum;

public class SincronizarReceitaServiceImp implements SincronizarReceitaInt {

	private static final String ERROR_FROM_FILE = "Erro ao mapear dados do arquivo";
	private static final String ERROR_SAVE_FILE = "Erro ao salvar arquivo";
	private final Logger log;
	private static final String CONTA = "conta";
	private static final String AGENCIA = "agencia";
	private static final String SALDO = "saldo";
	private static final String STATUS = "status";
	private static final String RESULTADO = "sincronizado";
	private static final String FORMATAR_CSV = "%s;%s;%s;%s;%s";
	private static final String SALVAR_FORMAT = "%n%s;%s;%s;%s;%s";
	private static final String SEPARAR_CAMPOS = ";";
	private final ReceitaService receitaService;

	public SincronizarReceitaServiceImp(ReceitaService receitaService) {
		this.receitaService = receitaService;
		this.log = LoggerFactory.getLogger(SincronizarReceitaServiceImp.class);
	}

	@Override
	public void processarArquivos(File arquivoCsv, File arquivoProcessado) {
		log.info("Processando Arquivo {}", arquivoCsv.getAbsoluteFile());

		try (InputStream fileInputStream = new FileInputStream(arquivoCsv);
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
				FileOutputStream fileOutputStream = new FileOutputStream(arquivoProcessado);
				BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream))) {

			DecimalFormat brazilCurrency = (DecimalFormat) NumberFormat.getNumberInstance(new Locale("pt", "br"));
			brazilCurrency.applyPattern("#0.00");
			bufferedWriter.write(String.format(FORMATAR_CSV, AGENCIA, CONTA, SALDO, STATUS, RESULTADO));
			bufferedReader.lines().skip(1).parallel().forEach(line -> {
				Conta conta = getContaFromString(line);
				syncConta(conta);
				salvarArquivoCsv(conta, bufferedWriter, brazilCurrency);
				log.info("Arquivo processado {} no Banco Central", conta.getConta());
			});

		} catch (IOException | IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
			throw new BusinessException(ERROR_FROM_FILE, e);
		}

		log.info("Arquivo salvo {}", arquivoProcessado.getAbsoluteFile());
	}

	private Conta getContaFromString(String line) {
		String[] c = line.split(SEPARAR_CAMPOS);
		return new Conta(c[0], c[1], Double.parseDouble(c[2].replace(",", ".")), StatusContaEnum.valueOf(c[3]));
	}

	private void syncConta(Conta conta) {
		try {
			boolean isContaAtualizada = receitaService.atualizarConta(conta.getAgencia(),
					conta.getConta().replace("-", ""), conta.getSaldo(), conta.getStatus().name());
			conta.setprocessada(isContaAtualizada ? ResultEnum.PROCESSADO : ResultEnum.NAO_PROCESSADO);
		} catch (InterruptedException | RuntimeException e) {
			conta.setprocessada(ResultEnum.ERRO);
		}
	}

	private void salvarArquivoCsv(Conta conta, BufferedWriter bufferedWriter, DecimalFormat currencyFormat)
			throws BusinessException {
		try {
			bufferedWriter.write(String.format(SALVAR_FORMAT, conta.getAgencia(), conta.getConta(),
					currencyFormat.format(conta.getSaldo()), conta.getStatus(), conta.getprocessada()));

		} catch (IOException e) {
			throw new BusinessException(ERROR_SAVE_FILE);
		}
	}
};
