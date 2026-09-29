const input = document.getElementById("wordInput");
const button = document.getElementById("checkButton");
const buttonLabel = button.querySelector(".check-label");
const message = document.getElementById("message");

const resultCard = document.getElementById("resultCard");
const result = document.getElementById("result");
const keyboard = document.getElementById("keyboard");
const characterCount = document.getElementById("characterCount");
const clearInput = document.getElementById("clearInput");

const keyRows = [
    ["Vowels", ["अ", "आ", "इ", "ई", "उ", "ऊ", "ए", "ऐ", "ओ", "औ", "अं", "अः"]],
    ["Consonants", ["क", "ख", "ग", "घ", "ङ", "च", "छ", "ज", "झ", "ञ"]],
    ["", ["ट", "ठ", "ड", "ढ", "ण", "त", "थ", "द", "ध", "न"]],
    ["", ["प", "फ", "ब", "भ", "म", "य", "र", "ल", "व"]],
    ["", ["श", "ष", "स", "ह"]],
    ["Matras & Signs", ["ा", "ि", "ी", "ु", "ू", "े", "ै", "ो", "ौ", "ं", "ः", "्"]]
];

for (const [label, rowKeys] of keyRows) {
    const group = document.createElement("div");
    group.className = "keyboard-group";
    if (label) {
        const groupLabel = document.createElement("span");
        groupLabel.className = "keyboard-group-label";
        groupLabel.textContent = label;
        group.appendChild(groupLabel);
    }
    const row = document.createElement("div");
    row.className = "keyboard-row";
    for (const character of rowKeys) {
        const key = document.createElement("button");
        key.type = "button";
        key.className = "key";
        key.textContent = character;
        key.setAttribute("aria-label", character);
        key.addEventListener("click", () => insertAtCursor(character));
        row.appendChild(key);
    }
    group.appendChild(row);
    keyboard.appendChild(group);
}

const actionRow = document.createElement("div");
actionRow.className = "keyboard-row keyboard-actions";
[
    ["Space", "space"],
    ["Backspace", "backspace"],
    ["Clear", "clear"]
].forEach(([label, action]) => {
    const key = document.createElement("button");
    key.type = "button";
    key.className = "key action-key";
    key.textContent = label;
    key.addEventListener("click", () => runKeyAction(action));
    actionRow.appendChild(key);
});
keyboard.appendChild(actionRow);

function updateCharacterCount() {
    const count = Array.from(input.value).length;
    characterCount.textContent = `${count} ${count === 1 ? "character" : "characters"}`;
}

input.addEventListener("input", updateCharacterCount);
clearInput.addEventListener("click", () => {
    input.value = "";
    input.focus();
    updateCharacterCount();
    message.textContent = "";
});
updateCharacterCount();

function insertAtCursor(text) {
    const start = input.selectionStart ?? input.value.length;
    const end = input.selectionEnd ?? input.value.length;
    input.setRangeText(text, start, end, "end");
    input.focus();
    input.dispatchEvent(new Event("input", { bubbles: true }));
}

function runKeyAction(action) {
    if (action === "space") {
        insertAtCursor(" ");
        return;
    }
    if (action === "clear") {
        input.value = "";
        input.focus();
        input.dispatchEvent(new Event("input", { bubbles: true }));
        return;
    }

    const start = input.selectionStart ?? input.value.length;
    const end = input.selectionEnd ?? input.value.length;
    let deleteStart = start;
    if (start === end && start > 0) {
        const previousCodePoint = Array.from(input.value.slice(0, start)).pop();
        deleteStart -= previousCodePoint.length;
    }
    input.setRangeText("", deleteStart, end, "end");
    input.focus();
    input.dispatchEvent(new Event("input", { bubbles: true }));
}

button.addEventListener("click", checkSpelling);

input.addEventListener("keydown", function (event) {

    if (event.key === "Enter") {
        checkSpelling();
    }

});

async function checkSpelling() {

    const word = input.value.trim();

    message.textContent = "";

    resultCard.classList.add("hidden");

    if (!word) {

        message.textContent =
            "Please enter a Hindi word.";

        return;
    }

    if (Array.from(word).length < 3) {
        message.textContent =
            "Please enter a Hindi word with at least 3 characters.";
        return;
    }

    button.disabled = true;

    buttonLabel.textContent = "Checking...";

    try {

        const response = await fetch(
            "/check",
            {
                method: "POST",

                headers: {
                    "Content-Type": "application/json"
                },

                body: JSON.stringify({
                    word: word
                })
            }
        );

        const data = await response.json();

        if (!response.ok) {

            throw new Error(
                data.error ||
                "Something went wrong."
            );
        }

        displayResult(data);

    } catch (error) {

        message.textContent =
            "Could not connect to the backend. " +
            "Make sure the Java server is running.";

        console.error(error);

    } finally {

        button.disabled = false;

        buttonLabel.textContent = "Check Spelling";
    }
}

function displayResult(data) {

    resultCard.classList.remove("hidden");

    result.innerHTML = "";

    if (data.correct) {

        const correctBox =
            document.createElement("div");

        correctBox.className = "result-banner success-banner";
        correctBox.innerHTML = "<span class=\"status-symbol\" aria-hidden=\"true\">✓</span>";
        const copy = document.createElement("div");
        copy.className = "status-copy";
        const title = document.createElement("h3");
        title.textContent = "Correct word";
        const word = document.createElement("span");
        word.className = "result-word";
        word.textContent = data.word;
        const detail = document.createElement("p");
        detail.textContent = "This word exists in the vocabulary.";
        copy.append(title, word, detail);
        correctBox.appendChild(copy);

        result.appendChild(correctBox);

        return;
    }

    const notFound =
        document.createElement("div");

    notFound.className = "result-banner warning-banner";
    notFound.innerHTML = "<span class=\"status-symbol\" aria-hidden=\"true\">!</span>";
    const notFoundCopy = document.createElement("div");
    notFoundCopy.className = "status-copy";
    const notFoundTitle = document.createElement("h3");
    notFoundTitle.textContent = "Word not found";
    const unknownWord = document.createElement("span");
    unknownWord.className = "result-word";
    unknownWord.textContent = data.word;
    notFoundCopy.append(notFoundTitle, unknownWord);
    notFound.appendChild(notFoundCopy);

    result.appendChild(notFound);

    if (
        !data.suggestions ||
        data.suggestions.length === 0
    ) {

        const noSuggestions =
            document.createElement("div");

        noSuggestions.className = "no-suggestions";

        noSuggestions.textContent =
            "No close suggestions found.";

        result.appendChild(noSuggestions);

        return;
    }

    const title =
        document.createElement("h3");

    title.className = "suggestions-heading";
    title.innerHTML = "<h3>Closest suggestions</h3><span>Ranked by similarity</span>";

    result.appendChild(title);

    const list =
        document.createElement("ol");

    list.className = "suggestions";

    data.suggestions.forEach(
        (suggestion, index) => {

            const item =
                document.createElement("li");

            const number =
                document.createElement("span");

            number.className = "number";

            number.textContent =
                String(index + 1).padStart(2, "0");

            const word =
                document.createElement("span");

            word.className = "suggestion-word";
            word.textContent = suggestion;

            const arrow = document.createElement("span");
            arrow.className = "suggestion-arrow";
            arrow.setAttribute("aria-hidden", "true");
            arrow.textContent = "→";

            item.appendChild(number);

            item.appendChild(word);
            item.appendChild(arrow);

            list.appendChild(item);
        }
    );

    result.appendChild(list);
}
