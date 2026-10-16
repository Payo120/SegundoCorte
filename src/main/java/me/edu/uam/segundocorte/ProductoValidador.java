package me.edu.uam.segundocorte;

import java.math.BigDecimal;
import java.util.Set;

public class ProductoValidador {

    private static final Set<String> CATEGORIAS_PERMITIDAS = Set.of("Papelería", "Tecnología", "Accesorios");
    private static final BigDecimal PRECIO_MIN = new BigDecimal("0.01");
    private static final BigDecimal PRECIO_MAX = new BigDecimal("99999999.99");

    public static String normalizarCodigo(String codigo) {
        return codigo == null ? "" : codigo.trim().toUpperCase();
    }

    public static String normalizarNombre(String nombre) {
        return nombre == null ? "" : nombre.trim();
    }

    public static Producto validarYConstruir(Integer idProducto, String rawCodigo, String rawNombre,
                                             String categoria, String rawPrecio, String rawStock) {
        String codigo = normalizarCodigo(rawCodigo);
        if (codigo.isEmpty() || codigo.length() > 10) {
            throw new IllegalArgumentException("El código es obligatorio y debe tener entre 1 y 10 caracteres (sin contar espacios laterales).");
        }

        String nombre = normalizarNombre(rawNombre);
        if (nombre.length() < 3 || nombre.length() > 80) {
            throw new IllegalArgumentException("El nombre debe tener entre 3 y 80 caracteres tras eliminar espacios laterales.");
        }

        if (categoria == null || !CATEGORIAS_PERMITIDAS.contains(categoria)) {
            throw new IllegalArgumentException("Debe seleccionar una categoría válida: Papelería, Tecnología o Accesorios.");
        }

        if (rawPrecio == null || rawPrecio.trim().isEmpty()) {
            throw new IllegalArgumentException("El precio es obligatorio.");
        }
        String textoPrecio = rawPrecio.trim();
        BigDecimal precio;
        try {
            precio = new BigDecimal(textoPrecio);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El precio debe ser un número válido (use punto como separador decimal, ej. 65.00).");
        }

        if (precio.scale() > 2) {
            throw new IllegalArgumentException("El precio no puede tener más de 2 decimales.");
        }
        if (precio.compareTo(PRECIO_MIN) < 0 || precio.compareTo(PRECIO_MAX) > 0) {
            throw new IllegalArgumentException("El precio debe ser mayor que 0 (entre 0.01 y 99999999.99).");
        }
        precio = precio.setScale(2);

        if (rawStock == null || rawStock.trim().isEmpty()) {
            throw new IllegalArgumentException("El stock es obligatorio.");
        }
        String textoStock = rawStock.trim();
        long stockLong;
        try {
            stockLong = Long.parseLong(textoStock);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El stock debe ser un número entero válido (sin decimales ni letras).");
        }

        if (stockLong < 0 || stockLong > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("El stock debe ser un número entero entre 0 y 2147483647.");
        }

        return new Producto(idProducto, codigo, nombre, categoria, precio, (int) stockLong);
    }
}