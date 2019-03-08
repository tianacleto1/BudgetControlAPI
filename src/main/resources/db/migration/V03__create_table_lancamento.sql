CREATE TABLE lancamento (
	codigo            BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	descricao         VARCHAR(50) NOT NULL,
	data_vencimento   DATE NOT NULL,
	data_pagamento    DATE,
	valor             DECIMAL(10,2) NOT NULL,
	observacao        VARCHAR(100),
	tipo              VARCHAR(20) NOT NULL,
	codigo_categoria  BIGINT(20) NOT NULL,
	codigo_pessoa     BIGINT(20) NOT NULL,
	FOREIGN KEY (codigo_categoria) REFERENCES categoria(codigo),
	FOREIGN KEY (codigo_pessoa) REFERENCES pessoa(codigo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO lancamento (descricao, data_vencimento, data_pagamento, valor, observacao, tipo, codigo_categoria, codigo_pessoa)
     VALUES ('Salário Mensal', '2017-06-10', null, 6500.00, 'Distribuição de Lucros', 'Receita', 5, 1);
INSERT INTO lancamento (descricao, data_vencimento, data_pagamento, valor, observacao, tipo, codigo_categoria, codigo_pessoa)
     VALUES ('Supermercado', '2017-02-10', '2017-02-10', 100.32, null, 'Despesa', 3, 2);
INSERT INTO lancamento (descricao, data_vencimento, data_pagamento, valor, observacao, tipo, codigo_categoria, codigo_pessoa)
     VALUES ('Top Club', '2017-06-10', null, 120, null, 'Receita', 5, 3);
INSERT INTO lancamento (descricao, data_vencimento, data_pagamento, valor, observacao, tipo, codigo_categoria, codigo_pessoa)
     VALUES ('CPFL', '2017-02-11', '2017-02-11', 110.44, 'Conta de Luz', 'Despesa', 5, 4);