package ml.ialegor.util.mutli.github

import com.fasterxml.jackson.annotation.JsonProperty
import feign.Param
import feign.QueryMap
import feign.RequestLine
import ml.ialegor.util.collection.FuturePage
import ml.ialegor.util.collection.PageRequest
import ml.ialegor.util.collection.PageResponse
import ml.ialegor.util.http.HttpClientBuilder
import ml.ialegor.util.http.feign.buildFeignClient
import ml.ialegor.util.logging.log
import ml.ialegor.util.logging.measure
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GitHubMultiTest {


    lateinit var gitHubDao: GitHubDao

    @BeforeEach
    fun before() {
        gitHubDao = GitHubDao(
            HttpClientBuilder()
                .url("https://api.github.com")
                .buildFeignClient()
        )
    }

    @Test
    fun testApi() {
        val futureTags = gitHubDao.getRepositoryTagsFuture("spring-projects/spring-boot")

        val allTags = futureTags.toList()

        val i = 5
    }
}

interface GitHubClient {

    @RequestLine("GET /repos/{name}/tags")
    fun getRepositoryTags(@Param("name") name: String, @QueryMap query: Map<String, Any>): List<GitHubTag>
}

class GitHubDao(
    private val client: GitHubClient,
) {
    private val log = log()
    private val options = FuturePage.Options(1, 30, 100)

    fun getRepositoryTags(
        name: String,
        page: PageRequest,
    ): List<GitHubTag> {
        val query = mapOf(
            "page" to page.page, "per_page" to page.size
        )
        return log
            .measure("Get repository tags: $page")
            .extract { client.getRepositoryTags(name, query) }
            .summary { "received $size items" }
    }

    fun getRepositoryTagsFuture(name: String, size: Int = options.defaultSize): FuturePage<GitHubTag> {
        return FuturePage(size, options) {
            val items = getRepositoryTags(name, this)
            return@FuturePage PageResponse(this, items)
        }
    }
}

class GitHubTag {
    @JsonProperty("name")
    lateinit var name: String

    @JsonProperty("zipball_url")
    lateinit var zipballUrl: String

    @JsonProperty("tarball_url")
    lateinit var tarballUrl: String

    @JsonProperty("commit")
    lateinit var commit: GitHubCommit

    @JsonProperty("node_id")
    lateinit var nodeId: String
}

class GitHubCommit {
    @JsonProperty("sha")
    lateinit var sha: String

    @JsonProperty("url")
    lateinit var url: String
}
