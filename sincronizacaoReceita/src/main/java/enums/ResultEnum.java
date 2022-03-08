package enums;

public enum ResultEnum {
	PROCESSADO("Sim"), NAO_PROCESSADO("Não"), ERRO("Erro");
	
	private final String label;

	ResultEnum(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return this.label;
    }
}
