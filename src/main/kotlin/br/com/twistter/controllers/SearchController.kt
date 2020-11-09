package br.com.twistter.controllers

import br.com.twistter.model.Follower
import br.com.twistter.model.User
import br.com.twistter.repository.FollowerRepository
import br.com.twistter.repository.UserRepository
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.ModelAndView
import java.security.Principal

@Controller
class SearchController(
    private val userRepository: UserRepository,
    private val followerRepository: FollowerRepository
) {

    @RequestMapping("/search")
    fun searchPage(): ModelAndView {
        return ModelAndView("search")
    }

    @RequestMapping("searchUsers")
    fun searchUsers(@RequestParam(value = "name", required = false) name: String, user: Principal): ModelAndView {
        val search = ModelAndView("search")
        val userConnected: User = userGet(user)
        val users: List<User?>? = userRepository.findAllUsersSearchCriteria(userConnected.id!!, name)
        val followedUsersId: Collection<Long?>? = userRepository.findFollowedUsersId(userConnected.id)
        for (userino in users!!) {
            if (followedUsersId!!.contains(userino!!.id)) {
                userino.alreadyFollows = true
            }
        }
        search.addObject("users", users)
        return search
    }

    @RequestMapping("/follow/{id}")
    fun followUser(user: Principal, @PathVariable(name = "id") id: Long): ModelAndView {
        val search = ModelAndView("redirect:/search")
        val userFollowing: User = userGet(user)
        val userFollowed: User = userRepository.findById(id).get()
        registerFollow(userFollowing, userFollowed)
        return search
    }

    @RequestMapping("/unfollow/{id}")
    fun unfollowUser(user: Principal, @PathVariable(name = "id") id: Long): ModelAndView {
        val search = ModelAndView("redirect:/search")
        val userFollowing: User = userGet(user)
        val userFollowed: User = userRepository.findById(id).get()
        registerUnfollow(userFollowing, userFollowed)
        return search
    }

    @ModelAttribute("users")
    fun allUsers(name: String?): Iterable<User?>? {
        return userRepository.findByNameContainingOrdered(name)
    }

    private fun registerFollow(userFollowing: User, userFollowed: User) {
        val follower = Follower()
        follower.user = userFollowing
        follower.followerId = userFollowed.id

        followerRepository.save(follower)
        userFollowed.followerCount = followerRepository.countUserFollowers(userFollowed.id!!)
        userFollowing.followingCount = followerRepository.countUserFollowing(userFollowing.id!!)
        userRepository.save(userFollowed)
        userRepository.save(userFollowing)
    }

    private fun registerUnfollow(userFollowing: User, userFollowed: User) {
        val follower: Follower = followerRepository.findByUserIdAndFollowerId(userFollowing.id!!, userFollowed.id!!)!!
        followerRepository.deleteById(follower.relationId)
        userFollowed.followerCount = followerRepository.countUserFollowers(userFollowed.id)
        userFollowing.followingCount = followerRepository.countUserFollowing(userFollowing.id)
        userRepository.save(userFollowed)
        userRepository.save(userFollowing)
    }

    private fun userGet(user: Principal): User {
        return userRepository.findByLogin(user.name)!!
    }
}