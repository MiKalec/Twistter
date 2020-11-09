package br.com.twistter.controllers

import br.com.twistter.model.Tweet
import br.com.twistter.model.User
import br.com.twistter.repository.TweetRepository
import br.com.twistter.repository.UserRepository
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView
import java.security.Principal
import javax.validation.Valid

@Controller
class HomeController(
    private val userRepository: UserRepository,
    private val tweetRepository: TweetRepository
) {

    @RequestMapping("/")
    fun homePage(tweet: Tweet, principal: Principal): ModelAndView {
        val mv = ModelAndView("home")
        mv.addObject("tweet", tweet)
        val user = getUserLogged(principal)
        fillUser(mv, user)
        fillTweets(mv, user)

        return mv
    }

    @PostMapping("newTweet")
    fun newTweet(user: Principal, @Valid tweet: Tweet): ModelAndView {
        val home = ModelAndView("redirect:/")

        val userToUpdate = getUserLogged(user)
        tweet.setUser(userToUpdate)

        home.addObject("tweet", tweet)
        tweetRepository.save(tweet)
        userToUpdate!!.tweetCount = countTweets(userToUpdate.id)
        userRepository.save(userToUpdate)
        return home
    }

    @RequestMapping("/delete/{id}")
    fun deleteTweet(user: Principal?, @PathVariable(name = "id") id: Long?): ModelAndView? {
        val home = ModelAndView("redirect:/")
        if (isTweetOwner(user, id)) {
            tweetRepository.deleteById(id)
            val userLogged = getUserLogged(user!!)
            userLogged!!.tweetCount = countTweets(userLogged.id)
            userRepository.save(userLogged)
        }
        return home
    }

    @RequestMapping("/retweet/{id}")
    fun retweet(user: Principal?, @PathVariable(name = "id") id: Long?): ModelAndView? {
        val home = ModelAndView("redirect:/")
        val retweeted = Tweet()
        retweeted.setTweetText(getTweet(id!!).tweetText)
        newTweet(user!!, retweeted)
        return home
    }

    private fun isTweetOwner(user: Principal?, id: Long?): Boolean {
        val tweetOwner: User? = getTweet(id!!).user
        val userSession = getUserLogged(user!!)
        return tweetOwner!! == userSession
    }

    private fun getTweet(id: Long) = tweetRepository.findById(id).get()

    private fun countTweets(id: Long?) =
        tweetRepository.countTweets(id)

    private fun fillUser(mv: ModelAndView, user: User?) {
        mv.addObject("user", user)
    }

    private fun fillTweets(mv: ModelAndView, user: User?) {
        val tweets = tweetRepository.findTweets(user!!.id)
        mv.addObject("tweets", tweets)
    }

    private fun getUserLogged(principal: Principal) =
        userRepository.findByLogin(principal.name)

}