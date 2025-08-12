import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GerenciadorProdutos {
    private static List<Produto> listaProdutos = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);
    private static int proximoCodigo = 1;

    public static void main(String[] args) {
        int opcao;
        do {
            exibirMenu();
            while (!scanner.hasNextInt()) {
                System.out.println("Entrada inválida. Digite um número.");
                scanner.next();
            }
            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    cadastrarProduto();
                    break;
                case 2:
                    editarProduto();
                    break;
                case 3:
                    excluirProduto();
                    break;
                case 4:
                    listarProdutos();
                    break;
                case 5:
                    venderProduto();
                    break;
                case 0:
                    System.out.println("Encerrando o programa...");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        } while (opcao != 0);
        scanner.close();
    }

    private static void exibirMenu() {
        System.out.println("\n--- Gerenciamento de Produtos ---");
        System.out.println("1 - Cadastrar produto");
        System.out.println("2 - Editar produto");
        System.out.println("3 - Excluir produto");
        System.out.println("4 - Listar produtos");
        System.out.println("5 - Vender produto");
        System.out.println("0 - Sair");
        System.out.print("Escolha uma opção: ");
    }

    private static void cadastrarProduto() {
        System.out.println("\n--- Cadastro de Novo Produto ---");
        System.out.print("Descrição: ");
        String descricao = scanner.nextLine();

        double preco = 0.0;
        boolean precoValido = false;
        while (!precoValido) {
            System.out.print("Preço: ");
            try {
                preco = Double.parseDouble(scanner.nextLine().replace(',', '.'));
                precoValido = true;
            } catch (NumberFormatException e) {
                System.out.println("Preço inválido. Digite um número decimal (ex: 10.22).");
            }
        }

        System.out.print("Caminho da imagem (opcional, pressione Enter para pular): ");
        String caminhoImagem = scanner.nextLine();

        if (!caminhoImagem.isEmpty() && !new File(caminhoImagem).exists()) {
            System.out.println("Aviso: O caminho da imagem informado não existe. O produto será cadastrado assim mesmo.");
        }

        Produto novoProduto = new Produto(proximoCodigo++, descricao, preco, caminhoImagem);
        listaProdutos.add(novoProduto);
        System.out.println("Produto '" + descricao + "' cadastrado com sucesso! Código: " + novoProduto.getCodigo());
    }

    private static void editarProduto() {
        System.out.println("\n--- Editar Produto ---");

        if (listaProdutos.isEmpty()) {
            System.out.println("Nenhum produto cadastrado para editar.");
            return;
        }

        System.out.println("Produtos cadastrados:");
        for (Produto p : listaProdutos) {
            System.out.printf("  [Código: %d] %s%n", p.getCodigo(), p.getDescricao());
        }

        System.out.println("\nDigite o código do produto para editar (ou 0 para cancelar):");
        int codigo = lerInteiro();

        if (codigo == 0) {
            System.out.println("Operação de edição cancelada.");
            return;
        }

        Produto produtoEncontrado = buscarProdutoPorCodigo(codigo);
        if (produtoEncontrado == null) {
            System.out.println("Produto com código " + codigo + " não encontrado.");
            return;
        }

        System.out.println("Produto encontrado: " + produtoEncontrado);

        System.out.print("Nova descrição (deixe em branco para manter a atual '" + produtoEncontrado.getDescricao() + "'): ");
        String novaDescricao = scanner.nextLine();
        if (!novaDescricao.isEmpty()) {
            produtoEncontrado.setDescricao(novaDescricao);
        }

        System.out.print("Novo preço (deixe em branco para manter o atual 'R$" + String.format("%.2f", produtoEncontrado.getPreco()) + "'): ");
        String novoPrecoStr = scanner.nextLine();
        if (!novoPrecoStr.isEmpty()) {
            try {
                double novoPreco = Double.parseDouble(novoPrecoStr.replace(',', '.'));
                produtoEncontrado.setPreco(novoPreco);
            } catch (NumberFormatException e) {
                System.out.println("Formato de preço inválido. O preço não foi alterado.");
            }
        }

        System.out.print("Novo caminho da imagem (deixe em branco para manter o atual '" + produtoEncontrado.getCaminhoImagem() + "'): ");
        String novoCaminhoImagem = scanner.nextLine();
        if (!novoCaminhoImagem.isEmpty()) {
            produtoEncontrado.setCaminhoImagem(novoCaminhoImagem);
        }

        System.out.println("Produto editado com sucesso!");
    }

    private static void excluirProduto() {
        System.out.println("\n--- Excluir Produto ---");
        System.out.print("Digite o código do produto para excluir: ");
        int codigo = lerInteiro();

        Produto produtoEncontrado = buscarProdutoPorCodigo(codigo);
        if (produtoEncontrado == null) {
            System.out.println("Produto com código " + codigo + " não encontrado.");
            return;
        }

        System.out.println("Produto a ser excluído: " + produtoEncontrado);
        System.out.print("Confirma a exclusão? (s/n): ");
        String confirmacao = scanner.nextLine();

        if (confirmacao.equalsIgnoreCase("s")) {
            listaProdutos.remove(produtoEncontrado);
            System.out.println("Produto excluído com sucesso!");
        } else {
            System.out.println("Operação de exclusão cancelada.");
        }
    }

    private static void listarProdutos() {
        System.out.println("\n--- Lista de Produtos ---");
        if (listaProdutos.isEmpty()) {
            System.out.println("Nenhum produto cadastrado.");
        } else {
            System.out.println("Produtos cadastrados:");
            for (Produto p : listaProdutos) {
                System.out.printf("  [Código: %d] %s%n", p.getCodigo(), p.getDescricao());
            }

            System.out.println("\nDigite o código do produto para ver detalhes (ou 0 para voltar ao menu):");
            int codigo = lerInteiro();

            if (codigo != 0) {
                Produto produtoEncontrado = buscarProdutoPorCodigo(codigo);
                if (produtoEncontrado != null) {
                    System.out.println("\n--- Detalhes do Produto ---");
                    System.out.println(produtoEncontrado);
                } else {
                    System.out.println("Produto com código " + codigo + " não encontrado.");
                }
            }
        }
    }

    private static void venderProduto() {
        System.out.println("\n--- Vender Produto ---");
        System.out.print("Digite o código do produto a ser vendido: ");
        int codigo = lerInteiro();

        Produto produtoEncontrado = buscarProdutoPorCodigo(codigo);
        if (produtoEncontrado == null) {
            System.out.println("Produto com código " + codigo + " não encontrado.");
            return;
        }

        System.out.print("Digite a quantidade a ser vendida: ");
        int quantidade = lerInteiro();

        double totalVenda = produtoEncontrado.getPreco() * quantidade;

        System.out.println("Venda realizada com sucesso!");
        System.out.println("--- Detalhes da Venda ---");
        System.out.println("Produto: " + produtoEncontrado.getDescricao());
        System.out.println("Preço Unitário: R$" + String.format("%.2f", produtoEncontrado.getPreco()));
        System.out.println("Quantidade: " + quantidade);
        System.out.println("Total: R$" + String.format("%.2f", totalVenda));
    }

    private static Produto buscarProdutoPorCodigo(int codigo) {
        for (Produto p : listaProdutos) {
            if (p.getCodigo() == codigo) {
                return p;
            }
        }
        return null;
    }

    private static int lerInteiro() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Digite um número inteiro.");
                System.out.print("Tente novamente: ");
            }
        }
    }
}