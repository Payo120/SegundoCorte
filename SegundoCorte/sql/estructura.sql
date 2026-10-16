DROP TABLE IF EXISTS producto;

CREATE TABLE producto (
                          id_producto INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                          codigo VARCHAR(10) NOT NULL UNIQUE,
                          nombre VARCHAR(80) NOT NULL,
                          categoria VARCHAR(20) NOT NULL,
                          precio NUMERIC(10,2) NOT NULL,
                          stock INTEGER NOT NULL,
                          CONSTRAINT chk_codigo_normalizado CHECK (
                              char_length(btrim(codigo)) BETWEEN 1 AND 10
                                  AND codigo = btrim(upper(codigo))
                              ),
                          CONSTRAINT chk_nombre_longitud CHECK (
                              char_length(btrim(nombre)) BETWEEN 3 AND 80
                              ),
                          CONSTRAINT chk_categoria_valida CHECK (
                              categoria IN ('Papelería', 'Tecnología', 'Accesorios')
                              ),
                          CONSTRAINT chk_precio_positivo CHECK (
                              precio > 0 AND precio <= 99999999.99
                              ),
                          CONSTRAINT chk_stock_no_negativo CHECK (
                              stock >= 0
                              )
);