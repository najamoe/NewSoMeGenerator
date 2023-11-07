document.addEventListener('DOMContentLoaded', function () {
    const SERVER_URL = 'http://localhost:2020/api/generate';

    document.getElementById('generatebtn').addEventListener('click', generateContent);

    async function generateContent() {
        const userPrompt = document.getElementById('userprompt').value;
        const URL = `${SERVER_URL}?topic=${userPrompt}`;
        const generatedContentDiv = document.getElementById('generatedContent');
        console.log("generating content function test");

        try {
            const response = await fetch(URL).then(handleHttpErrors);
            const jsonResponse = await response.json(); // Get the response as JSON
            const content = jsonResponse.answer; // Extract only the generated content
            const contentTextNode = document.createTextNode(content);
            generatedContentDiv.innerHTML = ''; // Clear any previous content
            generatedContentDiv.appendChild(contentTextNode); // Append the content to the div
            generatedContentDiv.style.display = 'block'; // Display the generated content box
            console.log("Content generated successfully");
        } catch (e) {
            generatedContentDiv.style.color = 'red';
            generatedContentDiv.innerText = e.message;
            console.error("Error:", e);
        }
    }

    async function handleHttpErrors(res) {
        if (!res.ok) {
            const errorResponse = await res.json();
            const msg = errorResponse.message ? errorResponse.message : 'No error details provided';
            throw new Error(msg); // Use "new" to create a new error
        }
        return res;
    }


});
