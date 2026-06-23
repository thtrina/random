
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
