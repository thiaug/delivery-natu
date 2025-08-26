-- Inserir clientes
INSERT INTO cliente (nome, email, telefone, endereco, data_cadastro, ativo) VALUES
('João Silva', 'joao@email.com', '(11) 99999-1111', 'Rua A, 123 - São Paulo/SP', NOW(), true),
('Maria Santos', 'maria@email.com', '(11) 99999-2222', 'Rua B, 456 - São Paulo/SP', NOW(),
true),
('Pedro Oliveira', 'pedro@email.com', '(11) 99999-3333', 'Rua C, 789 - São Paulo/SP', NOW(),
true); 

-- Inserir restaurantes (Naturais, Vegetarianos e Veganos)
INSERT INTO restaurante (nome, categoria, endereco, telefone, taxa_entrega, avaliacao, ativo)
VALUES
('Sabor Natural', 'Natural', 'Rua das Flores, 150 - São Paulo/SP', '(11) 4000-1111', 4.50, 4.6, true),
('Verde Vida', 'Vegetariano', 'Av. Consolação, 800 - São Paulo/SP', '(11) 4000-2222', 5.00, 4.7, true),
('Veg & Co.', 'Vegano', 'Rua Harmonia, 300 - São Paulo/SP', '(11) 4000-3333', 6.00, 4.9, true);

-- Inserir produtos
INSERT INTO produto (nome, descricao, preco, categoria, disponivel, restaurante_id) VALUES
-- Sabor Natural (Restaurante ID = 1)
('Salada Tropical', 'Mix de folhas, manga, castanhas e molho cítrico', 24.90, 'Salada', true, 1),
('Suco Verde', 'Suco detox com couve, maçã, gengibre e limão', 12.90, 'Bebida', true, 1),
('Quinoa com Legumes', 'Quinoa refogada com legumes frescos da estação', 29.90, 'Prato Principal', true, 1),

-- Verde Vida (Restaurante ID = 2)
('Hambúrguer Vegetariano', 'Pão integral, hambúrguer de grão-de-bico, queijo e molho especial', 32.90, 'Lanche', true, 2),
('Risoto de Cogumelos', 'Arroz arbóreo com cogumelos frescos e parmesão', 38.90, 'Prato Principal', true, 2),
('Omelete Mediterrânea', 'Ovos caipiras, espinafre, tomate seco e queijo feta', 27.90, 'Prato Principal', true, 2),

-- Veg & Co. (Restaurante ID = 3)
('Pizza Vegana de Legumes', 'Massa artesanal, molho de tomate, legumes grelhados e queijo vegano', 39.90, 'Pizza', true, 3),
('Tigela Buddha Bowl', 'Arroz integral, grão-de-bico, abóbora assada, brócolis e molho tahine', 34.90, 'Bowl', true, 3),
('Brownie Vegano', 'Brownie de cacau 70% com castanhas, sem glúten e sem lactose', 16.90, 'Sobremesa', true, 3);

-- Inserir pedidos de exemplo
INSERT INTO pedido (numero_pedido, data_pedido, status, valor_total, observacoes, cliente_id, restaurante_id) VALUES
('PED1234567893', NOW(), 'PENDENTE', 42.80, 'Molho da salada separado', 1, 1),
('PED1234567894', NOW(), 'CONFIRMADO', 65.80, 'Adicionar queijo extra no risoto', 2, 2),
('PED1234567895', NOW(), 'ENTREGUE', 56.80, 'Sem cebola no hambúrguer vegano', 3, 3);


INSERT INTO item_pedido (quantidade, preco_unitario, subtotal, pedido_id, produto_id) VALUES
-- Pedido 1 (Carla - Sabor Natural)
(1, 24.90, 24.90, 1, 1), -- Salada Tropical
(1, 17.90, 17.90, 1, 3), -- Quinoa com Legumes

-- Pedido 2 (Rafael - Verde Vida)
(1, 38.90, 38.90, 2, 4), -- Risoto de Cogumelos
(1, 26.90, 26.90, 2, 5), -- Omelete Mediterrânea

-- Pedido 3 (Ana - Veg & Co.)
(1, 39.90, 39.90, 3, 7), -- Pizza Vegana de Legumes
(1, 16.90, 16.90, 3, 9); -- Brownie Vegano