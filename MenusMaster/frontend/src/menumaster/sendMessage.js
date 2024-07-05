export async function sendMessage(message) {
    const response = await fetch('https://api.openai.com/v1/chat/completions', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer sk-proj-8KD8f4l5SCLPinn7PasnT3BlbkFJcssGarAG6Joi7QmDa77H`
        },
        body: JSON.stringify({
            model: "gpt-3.5-turbo",
            messages: [{ "role": "system", "content": "You are a helpful bot to give dishes recommendations and ways of making them by users' input." },{ "role": "user", "content": message }]
        })
    });

    if (!response.ok) {
        const errorData = await response.json();
        throw new Error(`API call failed with status: ${response.status}, details: ${JSON.stringify(errorData)}`);
    }

    const data = await response.json();
    return data.choices[0].message.content;
}