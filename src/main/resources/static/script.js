document.getElementById('send-button').addEventListener('click', sendMessage);

function sendMessage() {
    const userInput = document.getElementById('user-input').value;
    if (userInput.trim() === '') return;

    // Append user's message
    appendMessage(userInput, 'user');

    // Clear input field
    document.getElementById('user-input').value = '';

    fetch('/api/v1/prompt', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(userInput)
        })
        .then(response => response.json())
        .then(data => {
            console.log('data', data)
            appendMessage(data.message, 'model');
        })
        .catch(error => {
            console.error('Error:', error);
        });

}

function appendMessage(message, sender) {
    const chatWindow = document.getElementById('chat-window');
    const messageElement = document.createElement('div');
    messageElement.classList.add('message', sender);
    messageElement.textContent = message;
    chatWindow.appendChild(messageElement);
    chatWindow.scrollTop = chatWindow.scrollHeight;
}
