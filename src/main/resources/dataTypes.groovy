dataTypes {

    apiBase = "http://localhost:8080/olefs/api"

    jsonLDBase = "http://ole.kuali.org/standards/api"

    types = [
            locationLevel : [
                    contextURL:  "${jsonLDBase}/locationLevel.jsonld"
            ],
            location : [
                    contextURL: "${jsonLDBase}/location.jsonld"
            ],
            itemType : [

            ]

    ]


    environments {
        production {
            apiBase = "http://example.com/api"
        }
		
		
    }

}