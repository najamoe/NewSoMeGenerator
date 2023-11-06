const SERVER_URL = 'http://localhost:2020/api/';

document.getElementById('generatebtn').addEventListener('click', generateResponse);

async function generateResponse() {
    const URL = `${SERVER_URL}joke?about= + ${document.getElementById('about').value}`
    const spinner = document.getElementById('spinner1');
    const result = document.getElementById('result');
    result.style.color = "black";
    try {
        spinner.style.display = "block";
        const response = await fetch(URL).then(handleHttpErrors)
        document.getElementById('result').innerText = response.answer;
        //document.getElementById('about').value = ''
    } catch (e) {
        result.style.color = "red";
        result.innerText = e.message;
    }
    finally {
        spinner.style.display = "none";
    }
}



