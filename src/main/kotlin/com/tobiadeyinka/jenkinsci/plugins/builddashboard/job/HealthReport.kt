package com.tobiadeyinka.jenkinsci.plugins.builddashboard.job

/**
 * Health status entity for monitored jobs
 *
 * @property[iconUrl] Url for the health status icon
 * @property[description] Description of the health status
 */
class HealthReport constructor(val iconUrl: String, val description: String)