package br.com.twistter.model

import br.com.twistter.utils.StringHelper
import org.hibernate.annotations.CreationTimestamp
import org.joda.time.DateTime
import org.joda.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "tweets")
class Tweet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var tweetId: Long? = null
        private set

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    var user: User? = null
        private set

    @Column(nullable = false, length = 280)
    var tweetText: String? = null
        private set

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    var creationTime: Date? = null
        private set

    @get:Transient
    val formatTime: String?
        get() {
            val currentDateAndTime = LocalDateTime.now()
            val dateTime = currentDateAndTime.toDateTime()
            val tweetTime = DateTime(creationTime)
            val time = dateTime.millis - tweetTime.millis
            return StringHelper.formatDisplayTime(time)
        }

    constructor() {}

    constructor(tweetId: Long?, user: User?, tweetText: String?, creationTime: Date?) {
        this.tweetId = tweetId
        this.user = user
        this.tweetText = tweetText
        this.creationTime = creationTime
    }

    fun setUser(user: User?): Tweet {
        this.user = user
        return this
    }

    fun setTweetText(tweetText: String?): Tweet {
        this.tweetText = tweetText
        return this
    }

    fun setCreationTime(creationTime: Date?): Tweet {
        this.creationTime = creationTime
        return this
    }

    override fun toString(): String {
        return "Tweet(tweetId=$tweetId, tweetText=$tweetText, creationTime=$creationTime)"
    }

}
