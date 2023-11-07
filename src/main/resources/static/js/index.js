document.addEventListener('DOMContentLoaded', function () {
    const SERVER_URL = 'http://localhost:2020/api/generate';

    document.getElementById('generatebtn').addEventListener('click', generateContent);

    async function generateContent() {
        const userPrompt = document.getElementById('userprompt').value;
        const URL = `${SERVER_URL}?topic=${userPrompt}`;
        const generatedContentDiv = document.getElementById('generatedContent');

        try {
            const response = await fetch(URL).then(handleHttpErrors);
            const responseData = await response.json();
            generatedContentDiv.textContent = responseData.content;
        } catch (e) {
            generatedContentDiv.style.color = 'red';
            generatedContentDiv.innerText = e.message;
        }
    }

    async function handleHttpErrors(res) {
        if (!res.ok) {
            const errorResponse = await res.json();
            const msg = errorResponse.message ? errorResponse.message : 'No error details provided';
            throw new Error(msg); // Use "new" to create a new error
        }
        return res.json();
    }
});
