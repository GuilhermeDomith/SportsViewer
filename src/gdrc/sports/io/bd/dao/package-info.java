/**
 * Mantém todas as classe que são utilizadas para fazer a manipulaçao direta 
 * dos dados da aplicação no Banco de Dados. <br><br>
 * 
 * O <i>Modelo Relacional</i> adotado para o relacionamento das tabelas do banco de 
 * dados está descrito abaixo:<br><br>
 * 
 * Cliente(<u>email</u>, nome, sexo, altura, peso, dataDeNascimento) <br>
 * Exercicio(codigo,#<u>email</u>, <u>data</u>, <u>tempoInicio</u>, <u>tempoFim</u>, duracao, distancia, calorias, passos) <br>
 * Velocidade(<u>codigo</u>, velocidadeMaxima, velocidadeMedia) <br>
 * Elevacao(<u>codigo</u>, maiorElevacao, menorElevacao) <br>
 * Ritmo(<u>codigo</u>, ritmoMaximo, ritmoMedio) <br>
 * RitmoDetalhado(<u>codigo</u>, quilometro, ritmo) <br>
 * RitmoRitmoDetalhado(#<u>codigoRitmo</u>, #<u>codigoRitmoDetalhado</u>) <br><br>
 * ExercicioDetalhado(<u>#codigoExercicio</u>, #codVelocidade, #codRitmo, #codElevacao) <br><br>
 * 
 */
package gdrc.sports.io.bd.dao;