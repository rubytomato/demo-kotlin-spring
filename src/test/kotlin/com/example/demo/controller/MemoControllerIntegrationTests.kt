package com.example.demo.controller

import com.example.demo.DemoKotlinApplication
import com.example.demo.entity.Memo
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner

/**
 * 結合テスト
 */
@RunWith(SpringRunner::class)
@SpringBootTest(classes = [DemoKotlinApplication::class],
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MemoControllerIntegrationTests {

    class MemoList : MutableList<Memo> by ArrayList()

    @Autowired
    lateinit var testRestTemplate: TestRestTemplate

    @Test
    fun getOne() {
        val result = testRestTemplate.getForEntity("/memo/1", Memo::class.java)

        assertThat(result).isNotNull()
        assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(result.headers.contentType).isEqualTo(MediaType.APPLICATION_JSON_UTF8)
        assertThat(result.body.id).isEqualTo(1L)
    }

    @Test
    fun pagination() {
        val result = testRestTemplate.getForEntity("/memo/list?page={page}&size={size}", MemoList::class.java, 0, 3)

        assertThat(result).isNotNull()
        assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(result.headers.contentType).isEqualTo(MediaType.APPLICATION_JSON_UTF8)
        assertThat(result.body).hasSize(3)
    }

}