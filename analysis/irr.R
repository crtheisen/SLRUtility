# Clear
rm(list = ls())
cat("\014")

# Include Libraries
source("library.R")

# Initialize Libraries
init.libraries("irr")

##########################################
### Cohen's Kappa
##########################################
title.classifications <- read.csv("data/title.csv", header = F)
printline(paste("Title      ", get.cohenskappa(title.classifications)))

abstract.classifications <- read.csv("data/abstract.csv", header = F)
printline(paste("Abstract   ", get.cohenskappa(abstract.classifications)))

fulltext.classifications <- read.csv("data/fulltext.csv", header = F)
printline(paste("Full Text  ", get.cohenskappa(fulltext.classifications)))
