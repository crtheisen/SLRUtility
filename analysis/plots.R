# Clear
rm(list = ls())
cat("\014")

# Include Libraries
source("library.R")

# Initialize Libraries
init.libraries("plot")

##########################################
### Distributions
##########################################

##########################################
### Number of Papers by Class
dataset <- read.csv("data/class.csv", header = T) %>%
  filter(class != "Unavailable")

plot.dataset <- dataset %>%
  group_by(class) %>%
  summarize(count = n())

# Export Resolution   700 x 691
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
dataset <- read.csv("data/granularity.csv", header = T)

plot.dataset <- dataset %>%
  group_by(granularity) %>%
  summarize(count = n())

# Export Resolution   700 x 600
# File Name           distgranularity.eps
ggplot(plot.dataset, aes(x = granularity, y = count)) +
  geom_bar(stat = "identity", width = 1, color = "black", fill = "#d0d0d0") +
  geom_text(aes(label = count), vjust = -0.3) +
  labs(
    title = "Distribution of Number of Papers by Granularity",
    x = "Granularity", y = "# Papers"
  ) +
  get.theme()
