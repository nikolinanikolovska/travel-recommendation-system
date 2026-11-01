
document.addEventListener("DOMContentLoaded", () => {

    console.log("CHATBOT JS LOADED");

    const chatbotWidget =
        document.querySelector(".chatbot-widget");

    if (!chatbotWidget) {
        return;
    }


    // =========================================
    // CHATBOT ELEMENTS
    // =========================================

    const toggleButton =
        chatbotWidget.querySelector(".chatbot-toggle");

    const chatbotWindow =
        chatbotWidget.querySelector(".chatbot-window");

    const closeButton =
        chatbotWidget.querySelector(".chatbot-close");

    const form =
        chatbotWidget.querySelector(".chatbot-form");

    const input =
        chatbotWidget.querySelector(".chatbot-input");

    const messagesContainer =
        chatbotWidget.querySelector("#chatbotMessages");

    const recommendationsContainer =
        chatbotWidget.querySelector("#chatbotRecommendations");


    // =========================================
    // OPEN CHATBOT
    // =========================================

    toggleButton.addEventListener("click", () => {

        chatbotWindow.classList.add("open");
        toggleButton.classList.add("open");

    });


    // =========================================
    // CLOSE CHATBOT
    // =========================================

    closeButton.addEventListener("click", () => {

        chatbotWindow.classList.remove("open");
        toggleButton.classList.remove("open");

    });


    // =========================================
    // LOAD CHATBOT STATE
    // =========================================

    loadChatbotState();


    async function loadChatbotState() {

        try {

            const response =
                await fetch("/chatbot/api", {
                    method: "GET",
                    headers: {
                        "Accept": "application/json"
                    }
                });


            if (!response.ok) {

                throw new Error(
                    "Failed to load chatbot state."
                );

            }


            const data =
                await response.json();


            renderMessages(data.messages);

            renderRecommendations(
                data.recommendations
            );


        } catch (error) {

            console.error(
                "Chatbot error:",
                error
            );

        }

    }


    // =========================================
    // SEND MESSAGE
    // =========================================

    form.addEventListener("submit", async (event) => {

        event.preventDefault();


        const message =
            input.value.trim();


        if (!message) {
            return;
        }


        // Immediately show user's message

        addMessage(
            "USER",
            message
        );


        // Clear input

        input.value = "";


        // Disable input while waiting

        input.disabled = true;

        const sendButton =
            form.querySelector(".chatbot-send");

        sendButton.disabled = true;


        try {

            const response =
                await fetch("/chatbot/api", {
                    method: "POST",

                    headers: {
                        "Content-Type":
                            "application/json",

                        "Accept":
                            "application/json"
                    },

                    body: JSON.stringify({
                        message: message
                    })
                });


            if (!response.ok) {

                throw new Error(
                    "Failed to send chatbot message."
                );

            }


            const data =
                await response.json();


            // Add bot response

            addMessage(
                "BOT",
                data.message
            );


            // Update recommendations

            renderRecommendations(
                data.recommendations
            );


        } catch (error) {

            console.error(
                "Chatbot error:",
                error
            );


            addMessage(
                "BOT",
                "Sorry, something went wrong. Please try again."
            );

        } finally {

            input.disabled = false;

            sendButton.disabled = false;

            input.focus();

        }

    });


    // =========================================
    // ADD SINGLE MESSAGE
    // =========================================

    function addMessage(
        sender,
        message
    ) {

        const messageElement =
            document.createElement("div");

        messageElement.classList.add(
            "chat-message"
        );


        if (sender === "USER") {

            messageElement.classList.add(
                "user"
            );

        } else {

            messageElement.classList.add(
                "bot"
            );

        }


        const senderElement =
            document.createElement("strong");


        senderElement.textContent =
            sender === "USER"
                ? "You"
                : "Travel Assistant";


        const textElement =
            document.createElement("span");


        textElement.textContent =
            message;


        messageElement.appendChild(
            senderElement
        );

        messageElement.appendChild(
            textElement
        );


        messagesContainer.appendChild(
            messageElement
        );


        // Scroll to the newest message

        messagesContainer.parentElement.scrollTop =
            messagesContainer.parentElement.scrollHeight;

    }


    // =========================================
    // RENDER ALL MESSAGES
    // =========================================

    function renderMessages(messages) {

        messagesContainer.innerHTML = "";


        if (!messages || messages.length === 0) {
            return;
        }


        messages.forEach(chatMessage => {

            addMessage(
                chatMessage.sender,
                chatMessage.message
            );

        });

    }


    // =========================================
    // RENDER RECOMMENDATIONS
    // =========================================

    function renderRecommendations(
        recommendations
    ) {

        recommendationsContainer.innerHTML = "";


        if (
            !recommendations ||
            recommendations.length === 0
        ) {
            return;
        }


        const recommendationsSection =
            document.createElement("section");

        recommendationsSection.classList.add(
            "chatbot-recommendations"
        );


        const header =
            document.createElement("div");

        header.classList.add(
            "recommendations-header"
        );


        const label =
            document.createElement("span");

        label.classList.add(
            "recommendation-form-label"
        );

        label.textContent =
            "RECOMMENDED FOR YOU";


        const title =
            document.createElement("h2");

        title.textContent =
            "Destinations you might like";


        const description =
            document.createElement("p");

        description.textContent =
            "These destinations were selected based on your preferences.";


        header.appendChild(label);

        header.appendChild(title);

        header.appendChild(description);


        const destinationGrid =
            document.createElement("div");

        destinationGrid.classList.add(
            "destination-grid"
        );


        recommendations.forEach(destination => {

            const card =
                document.createElement("article");

            card.classList.add(
                "destination-card"
            );


            // =========================================
            // IMAGE
            // =========================================

            const imageContainer =
                document.createElement("div");

            imageContainer.classList.add(
                "destination-image"
            );


            if (
                destination.imageUrl &&
                destination.imageUrl.trim() !== ""
            ) {

                const image =
                    document.createElement("img");

                image.src =
                    destination.imageUrl;

                image.alt =
                    destination.name || "Destination";

                imageContainer.appendChild(
                    image
                );

            } else {

                const placeholder =
                    document.createElement("div");

                placeholder.classList.add(
                    "destination-image-placeholder"
                );


                const icon =
                    document.createElement("span");

                icon.textContent = "✈️";


                placeholder.appendChild(icon);

                imageContainer.appendChild(
                    placeholder
                );

            }


            // =========================================
            // CONTENT
            // =========================================

            const content =
                document.createElement("div");

            content.classList.add(
                "destination-content"
            );


            // =========================================
            // TITLE
            // =========================================

            const destinationTitle =
                document.createElement("div");

            destinationTitle.classList.add(
                "destination-title"
            );


            const name =
                document.createElement("h3");

            name.textContent =
                destination.name || "";


            const country =
                document.createElement("span");

            country.textContent =
                destination.country || "";


            destinationTitle.appendChild(name);

            destinationTitle.appendChild(country);


            // =========================================
            // DESCRIPTION
            // =========================================

            const destinationDescription =
                document.createElement("p");

            destinationDescription.classList.add(
                "destination-description"
            );

            destinationDescription.textContent =
                destination.description || "";


            // =========================================
            // META
            // =========================================

            const meta =
                document.createElement("div");

            meta.classList.add(
                "destination-meta"
            );


            // DAILY COST

            const dailyCost =
                document.createElement("div");

            dailyCost.classList.add(
                "destination-meta-item"
            );


            const dailyCostLabel =
                document.createElement("span");

            dailyCostLabel.classList.add(
                "meta-label"
            );

            dailyCostLabel.textContent =
                "DAILY COST";


            const dailyCostValue =
                document.createElement("strong");

            dailyCostValue.textContent =
                "€ " +
                (
                    destination.averageDailyCost ?? ""
                );


            dailyCost.appendChild(
                dailyCostLabel
            );

            dailyCost.appendChild(
                dailyCostValue
            );


            // RECOMMENDED DURATION

            const duration =
                document.createElement("div");

            duration.classList.add(
                "destination-meta-item"
            );


            const durationLabel =
                document.createElement("span");

            durationLabel.classList.add(
                "meta-label"
            );

            durationLabel.textContent =
                "RECOMMENDED";


            const durationValue =
                document.createElement("strong");

            durationValue.textContent =
                (
                    destination.recommendedDuration ?? ""
                ) +
                " days";


            duration.appendChild(
                durationLabel
            );

            duration.appendChild(
                durationValue
            );


            meta.appendChild(dailyCost);

            meta.appendChild(duration);


            // =========================================
            // ADD CONTENT
            // =========================================

            content.appendChild(
                destinationTitle
            );

            content.appendChild(
                destinationDescription
            );

            content.appendChild(meta);


            // =========================================
            // ADD CARD
            // =========================================

            card.appendChild(
                imageContainer
            );

            card.appendChild(
                content
            );


            destinationGrid.appendChild(
                card
            );

        });


        recommendationsSection.appendChild(
            header
        );

        recommendationsSection.appendChild(
            destinationGrid
        );


        recommendationsContainer.appendChild(
            recommendationsSection
        );

    }

});
