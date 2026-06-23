
// Get the raw request body
let rawBody = pm.request.body.raw;

// Convert string to JSON
let body = {};
try {
    body = JSON.parse(rawBody);
} catch (e) {
    console.log("Invalid JSON body");
}

// List of attributes that cause bad requests
const badAttributes = [
    "badAttribute",
    "birthright",
    "accessModelMetadata"
];

// Function to recursively remove bad attributes
function removeBadAttributes(obj) {
    if (Array.isArray(obj)) {
        return obj.map(removeBadAttributes);
    } else if (typeof obj === "object" && obj !== null) {
        let newObj = {};
        Object.keys(obj).forEach(key => {
            if (!badAttributes.includes(key)) {
                newObj[key] = removeBadAttributes(obj[key]);
            } else {
                console.log(`Removed attribute: ${key}`);
            }
        });
        return newObj;
    }
    return obj;
}

// Clean the payload
let cleanedBody = removeBadAttributes(body);

// Update request body
pm.request.body.raw = JSON.stringify(cleanedBody, null, 2);



////////////// how to find the attributes that causing the bad request ..............................

{"detailCode":"400.0 Bad request syntax","trackingId":"280a1c25f73b4a65982068ea0d92fd8d","messages":[{"locale":"en-US","localeOrigin":"DEFAULT","text":"The request could not be parsed."},{"locale":"und","localeOrigin":"REQUEST","text":"The request could not be parsed."}],"causes":[]}

..............................................................Pre-request Script to Test Attributes One-by-One

let originalBody = JSON.parse(pm.request.body.raw);

// Flatten keys (top-level only for simplicity)
let keys = Object.keys(originalBody);

let workingKey = null;

for (let i = 0; i < keys.length; i++) {
    let testBody = JSON.parse(JSON.stringify(originalBody));

    delete testBody[keys[i]];

    pm.sendRequest({
        url: pm.request.url.toString(),
        method: pm.request.method,
        header: pm.request.headers.toObject(),
        body: {
            mode: 'raw',
            raw: JSON.stringify(testBody)
        }
    }, function (err, res) {
        if (res && res.code !== 400) {
            console.log("🚨 Problem attribute:", keys[i]);
        }
    });
}
--------------------payload trying to request 
[
    {
        "name": "NOACCESS",
        "type": "ENTITLEMENT",
        "id": "75dd0b56c3ac391ca119a70b266aaaf3",        
        "accessRequestPhases": [
            {
                
                "finished": null,
                "name": "SOD_PHASE",
                "result": null,
                "state": "EXECUTING",
                "phaseReference": "sodViolationContext"
            }
        ],
        "accountActivityItemId": "9a21a4b07e994470bf1a7cb798a24a67",
        "requestType": "GRANT_ACCESS",        
        "requester": {
            "type": "IDENTITY",
            "id": "4d41d33c4c49450a8ee3ed3f8b341fc3",
            "name": "ADAPA, SAI DURGA"
        },
        "requestedFor": {
            "type": "IDENTITY",
            "id": "ae785a5827a741339901fd207db2eab3"
           
        },
        "requesterComment": null,
        "sodViolationContext": null,
        "provisioningDetails": null,
        "preApprovalTriggerDetails": null,
        "description": "COMMON NO ACCESS DEFAULT GROUP -             DO NOT PERMIT ACCESS",
        "removeDate": null,
        "startDate": null,
        "cancelable": true,       
        "clientMetadata": null,
        "form": null,
        "identityType": "HUMAN",
        "accessRequestContext": null,
        "requestedAccounts": [
            {
                "sourceName": "RACF",
                "accountId": null,
                "accountUuid": null,
                "type": "ACCOUNT",
                "id": null,
                "name": null
            }
        ],
        "privilegeLevel": "None",
        "jitDetails": null,
        "privileged": null
    }
]

-------------- use this instead
{
  "requestedFor": [
    "ae785a5827a741339901fd207db2eab3"
  ],
  "requestType": "GRANT_ACCESS",
  "requestedItems": [
    {
      "id": "75dd0b56c3ac391ca119a70b266aaaf3",
      "type": "ENTITLEMENT"
    }
  ],
  "comment": "Requesting NOACCESS entitlement"
}
------------------------------------------------------------------------------- or prescript to exclude attributes
const badAttributes = [
    "id",
    "accessRequestPhases",
    "accountActivityItemId",
    "cancelable",
    "clientMetadata",
    "identityType",
    "jitDetails",
    "privileged",
    "provisioningDetails",
    "preApprovalTriggerDetails",
    "accessRequestContext",
    "requester",
    "requestedAccounts"
];
