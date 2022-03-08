package br.com.sicredi.sincronizacaoReceita;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import enums.ResultEnum;
import enums.StatusContaEnum;

public class SincronizarReceitaServiceTest {
	private SincronizarReceitaServiceImp sincronizarReceitaServiceImp;
    private String accountPath = "src/test/resources/";

    @BeforeEach
    void setUp() {
    	sincronizarReceitaServiceImp = new SincronizarReceitaServiceImp(new ReceitaService());
    }

    @Test
    void syncAccountsFromFile() throws IOException {
        File arquivoCsv = new File(accountPath + "contas-teste.csv");
        File arquivoProcessado = new File(accountPath + "contas-teste_sincronizado.csv");
        arquivoProcessado.delete();
        sincronizarReceitaServiceImp.processarArquivos(arquivoCsv, arquivoProcessado);

        assertTrue(arquivoProcessado.isFile());
        Set<Conta> contaResult = getAccountsFromCsvFile(arquivoProcessado);
        assertNotNull(contaResult);
        assertEquals(5, contaResult.size());
        assertFalse(contaResult.stream().anyMatch(conta -> conta.getprocessada() == null));
    }

    @Test 
    void sincContaSemArquivo() {
        File arquivoCsv = new File(accountPath + "contas-teste-nao-existe.csv");
        File arquivoProcessado = new File(accountPath + "contas-teste_sincronizado.csv");

        Exception errorType = null;
        try {
        	sincronizarReceitaServiceImp.processarArquivos(arquivoCsv, arquivoProcessado);
        } catch (Exception e) {
            errorType = e;
            assertEquals("Erro ao mapear dados do arquivo", e.getMessage());
        }

        assert errorType instanceof BusinessException;
    }

    private Set<Conta> getAccountsFromCsvFile(File file) throws IOException {
        try (InputStream fileInputStream = new FileInputStream(file);
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream))) {

            return bufferedReader.lines()
                    .skip(1)
                    .map(line -> {
                        String[] c = line.split(";");
                        Conta conta = new Conta(c[0], c[1], Double.parseDouble(c[2].replace(",", ".")), StatusContaEnum.valueOf(c[3]));
                        conta.setprocessada(Arrays.stream(ResultEnum.values())
                                        .filter(ResultEnum -> ResultEnum.toString().contentEquals(c[4])).findFirst().get());
                        return conta;
                    })
                    .collect(Collectors.toSet());
        }
    }

}
