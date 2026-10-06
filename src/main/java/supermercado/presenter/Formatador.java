package supermercado.presenter;

import supermercado.excecao.ValidacaoException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Locale;
import java.util.regex.Pattern;

public final class Formatador {

    private static final Locale PT_BR = Locale.of("pt", "BR");
    private static final DateTimeFormatter FORMATO_DATA =
            DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);

    private static final Pattern DECIMAL_COM_MILHAR = Pattern.compile("-?[1-9]\\d{0,2}(\\.\\d{3})+(,\\d+)?");
    private static final Pattern DECIMAL_VIRGULA = Pattern.compile("-?\\d+(,\\d+)?");
    private static final Pattern DECIMAL_PONTO = Pattern.compile("-?\\d+\\.\\d+");

    private Formatador() {
    }

    public static String formatarMoeda(Double valor) {
        return valor == null ? "" : "R$ " + formatarDecimal(valor);
    }

    public static String formatarDecimal(Double valor) {
        if (valor == null) {
            return "";
        }
        DecimalFormat formato = new DecimalFormat("#,##0.00", DecimalFormatSymbols.getInstance(PT_BR));
        return formato.format(valor);
    }

    public static String formatarData(LocalDate data) {
        return data == null ? "" : data.format(FORMATO_DATA);
    }

    public static Double lerDecimal(String texto, String campo) throws ValidacaoException {
        if (texto == null || texto.isBlank()) {
            return null;
        }
        String valor = texto.replace("R$", "").replaceAll("[\\s\\u00A0]", "");
        String normalizado;
        if (DECIMAL_COM_MILHAR.matcher(valor).matches()) {
            normalizado = valor.replace(".", "").replace(',', '.');
        } else if (DECIMAL_VIRGULA.matcher(valor).matches()) {
            normalizado = valor.replace(',', '.');
        } else if (DECIMAL_PONTO.matcher(valor).matches()) {
            normalizado = valor;
        } else {
            throw new ValidacaoException("O campo \"" + campo + "\" deve conter um número válido, como 1.234,56.");
        }
        BigDecimal numero = new BigDecimal(normalizado);
        if (numero.stripTrailingZeros().scale() > 2) {
            throw new ValidacaoException("O campo \"" + campo + "\" deve ter no máximo duas casas decimais.");
        }
        return numero.doubleValue();
    }

    public static LocalDate lerData(String texto, String campo) throws ValidacaoException {
        if (texto == null || texto.isBlank()) {
            return null;
        }
        try {
            return LocalDate.parse(texto.trim(), FORMATO_DATA);
        } catch (DateTimeParseException e) {
            throw new ValidacaoException("O campo \"" + campo + "\" deve conter uma data válida no formato dd/mm/aaaa.");
        }
    }
}
