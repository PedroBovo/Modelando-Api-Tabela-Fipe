package br.com.pedro.fipe.principal;

import br.com.pedro.fipe.model.Dados;
import br.com.pedro.fipe.model.Modelos;
import br.com.pedro.fipe.model.Veiculo;
import br.com.pedro.fipe.sevice.ConverteDados;
import br.com.pedro.fipe.sevice.ObtemDados;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {
    private Scanner s = new Scanner(System.in);
    private ObtemDados obtem = new ObtemDados();
    private ConverteDados converte = new ConverteDados();
    private final String URL_BASE = "https://parallelum.com.br/fipe/api/v1/";

    public void exibeMenu(){
        var menu= """
                ***Opcões***
                
                Carro
                Moto
                Caminhão
                
                Digite uma das opções para consultar:
                ************
                """;
        System.out.println(menu);
        var opcao = s.nextLine();
        String endereco = "";
        if (opcao.toLowerCase().contains("carr")){
            endereco = URL_BASE + "carros/marcas" ;
        }else if(opcao.toLowerCase().contains("mot")){
            endereco = URL_BASE + "motos/marcas" ;
        } else if(opcao.toLowerCase().contains("cami")){
            endereco = URL_BASE + "caminhoes/marcas" ;
        }
        var json = obtem.obtemDados(endereco);
        System.out.println(json);

       var marcas = converte.obterLista(json, Dados.class);

//        System.out.println(marcas);

        marcas.stream()
                .sorted(Comparator.comparing(Dados::getCodigo))
                .forEach(System.out::println);

        System.out.println("Digite o codigo da marca para ver os modelos: ");
        var codigo = s.nextLine();
        var enderecoModelos = endereco + "/" + codigo + "/modelos";
        json = obtem.obtemDados(enderecoModelos);
        System.out.println(json);

        var modelos = converte.converteDados(json, Modelos.class);

        System.out.println("Medelos dessa marcar: \n");
        modelos.modelos().stream()
                .sorted(Comparator.comparing(Dados::getCodigo))
                .forEach(System.out::println);

        System.out.println("\n Digite um trecho do nome do carro a ser buscado: ");
        var trechoCarro = s.nextLine();

        List<Dados> modelosFiltrados = modelos.modelos().stream()
                .filter(m -> m.getNome().toLowerCase().contains(trechoCarro.toLowerCase()))
                .collect(Collectors.toList());

        System.out.println("\n modelos filtrados: ");

        modelosFiltrados.forEach(System.out::println);

        System.out.println("\n Digite o codigo do modelo: ");
        var codigoModelo = s.nextLine();
        enderecoModelos = enderecoModelos + "/" + codigoModelo + "/anos";
        json = obtem.obtemDados(enderecoModelos );

        List<Dados> anosModelos = converte.obterLista(json, Dados.class);


        List<Veiculo> veiculos = new ArrayList<>();
        for (int i = 0; i < anosModelos.size() ; i++) {
            var enderecoAnos = enderecoModelos + "/" + anosModelos.get(i).getCodigo();
            json = obtem.obtemDados(enderecoAnos);
            Veiculo veiculo = converte.converteDados(json, Veiculo.class);
            veiculos.add(veiculo);
        }
        System.out.println("\nTodos os veiculos filtrados com avaliações por ano");

        veiculos.forEach(System.out::println);


    }
}
