package org.openmrs.modulus

import grails.transaction.Transactional
import org.apache.commons.io.FilenameUtils
import org.codehaus.groovy.grails.web.mapping.LinkGenerator

@Transactional
class DownloadLinkGeneratorService {

    LinkGenerator grailsLinkGenerator

    def URL(String controller, long id, String filename) {

        def link = grailsLinkGenerator.link(
                controller: controller,
                action: 'download',
                id: id,
                params: [filename: filename],
                mapping: 'downloadResource',
        )

        link
    }

    def URI(String controller, long id, String filename) {
        def base = grailsLinkGenerator.getServerBaseURL()
        def resource = URI(controller, id, filename)

        resource.replace(base, '')
    }
}
