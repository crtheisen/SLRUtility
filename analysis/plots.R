# Clear
rm(list = ls())
cat("\014")

# Include Constants
source("constants.R")

# Include Libraries
source("library.R")

# Initialize Libraries
init.libraries("plot")

##########################################
### Distributions
##########################################

##########################################
### Number of Papers by Class
dataset <- read.csv("data/plots/class.csv", header = T) %>%
  filter(class != "Unavailable", class != "Duplicate", class != "Pending")

dataset$class <- factor(dataset$class, levels = PAPER.CLASSES)

plot.dataset <- dataset %>%
  group_by(class) %>%
  summarize(count = n()) %>%
  arrange(count)

# Export Resolution   600 x 575
# File Name           distclass.eps
ggplot(plot.dataset, aes(x = class, y = count)) +
  geom_bar(stat = "identity", width = 1, color = "black", fill = "#d0d0d0") +
  geom_text(aes(label = count), vjust = -0.3) +
  labs(
    title = "Distribution of Number of Papers by Class",
    x = "Class", y = "# Papers"
  ) +
  get.theme()

##########################################
### Number of Papers by Granularity
dataset <- read.csv("data/plots/granularity.csv", header = T)

dataset$granularity <- factor(dataset$granularity, levels = PAPER.GRANULARITY)

plot.dataset <- dataset %>%
  group_by(granularity) %>%
  summarize(count = n()) %>%
  arrange(count)

# Export Resolution   650 x 550
# File Name           distgranularity.eps
ggplot(plot.dataset, aes(x = granularity, y = count)) +
  geom_bar(stat = "identity", width = 1, color = "black", fill = "#d0d0d0") +
  geom_text(aes(label = count), vjust = -0.3) +
  labs(
    title = "Distribution of Number of Papers by Granularity",
    x = "Granularity", y = "# Papers"
  ) +
  get.theme()
