package br.com.alura.forumapi.service

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class TopicServiceTest {
    @Nested
    @DisplayName("findAll")
    inner class FindAllTest {
        @Test
        fun `Deve retornar uma lista de topicos com os dados corretos dos topicos recuperados pelo nome curso se informado`() {
        }

        @Test
        fun `Deve retornar uma lista de topicos GetTopicDto com os tópicos recuperados sem filtros se o nome do curso nao for informado`() {
        }
    }

    @Nested
    @DisplayName("findById")
    inner class FindByIdTest {
        @Test
        fun `Deve retornar um topico a partir do id informado se ele estiver registrado no banco de dados`() {
        }

        @Test
        fun `Deve lancar uma excecao se nao existir um topico registrado para o id informado`() {
        }
    }

    @Nested
    @DisplayName("create")
    inner class CreateTest {
        @Test
        fun `Deve criar um topico no banco de dados com os dados corretos e retornar os dados corretos`() {
        }

        @Test
        fun `Deve lancar uma excecao se nao existir registrado um curso registrado para o id informado`() {
        }

        @Test
        fun `Deve lancar uma excecao se nao existir registrado um usuario registrado para o id informado`() {
        }
    }

    @Nested
    @DisplayName("update")
    inner class UpdateTest {
        @Test
        fun `Deve atualizar um topico no banco de dados com os dados corretos e retornar os dados corretos`() {
        }

        @Test
        fun `Deve lancar uma excecao se nao existir um topico registrado para o id informado`() {
        }
    }

    @Nested
    @DisplayName("remove")
    inner class RemoveTest {
        @Test
        fun `Deve remover um topico no banco de dados e retornar o id do topico`() {
        }

        @Test
        fun `Deve lancar uma excecao se nao existir um topico registrado para o id informado`() {
        }
    }

    @Nested
    @DisplayName("reportByCategory")
    inner class ReportByCategoryTest {
        @Test
        fun `Deve retornar uma lista de topicos agrupados por categoria`() {
        }
    }

    @Nested
    @DisplayName("reportCountByCategory")
    inner class ReportCountByCategoryTest {
        @Test
        fun `Deve retornar uma lista da contagem de topicos por categoria`() {
        }
    }
}
