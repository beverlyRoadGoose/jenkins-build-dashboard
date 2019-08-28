package me.tobiadeyinka.jenkinsci.plugins.builddashboard.security.csrf

import jenkins.model.Jenkins
import hudson.security.csrf.CrumbIssuer
import com.fasterxml.jackson.annotation.JsonIgnore

class RemoteRequestCrumb {

    @JsonIgnore private val crumbIssuer: CrumbIssuer? = Jenkins.getInstance()?.getCrumbIssuer()
    val fieldName: String? = crumbIssuer?.crumbRequestField
    val crumbValue: String? = crumbIssuer?.crumb

}