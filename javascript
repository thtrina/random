
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

