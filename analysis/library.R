init.libraries <- function(configuration){
  suppressPackageStartupMessages(library("dplyr"))

  if(configuration == "irr"){
    suppressPackageStartupMessages(library("irr"))
  } else if(configuration == "plot"){
    suppressPackageStartupMessages(library("ggplot2"))
  } else {
    stop(paste("Unknown configuration:", configuration))
  }
}

printline <- function(text){
  cat(paste(text, "\n"))
}

get.cohenskappa <- function(classification, alpha = 0.05){
  if(ncol(classification) > 2){
    stop("Cohens's Kappa is for two raters only. Use Fliess's Kappa instead.")
  }
  agreement <- kappa2(classification)
  if(agreement$p.value > alpha){
    warning("Agreement assessment was statistically insignificant")
  }
  return(agreement$value)
}

get.theme <- function(){
  plot.theme <-
    theme_bw() +
    theme(
      plot.title = element_text(
        size = 14, face = "bold", margin = margin(5,0,25,0)
      ),
      axis.text.x = element_text(size = 10, angle = 50, vjust = 1, hjust = 1),
      axis.title.x = element_text(face = "bold", margin = margin(15,0,5,0)),
      axis.text.y = element_text(size = 10),
      axis.title.y = element_text(face = "bold", margin = margin(0,15,0,5)),
      strip.text.x = element_text(size = 10, face = "bold"),
      legend.position = "bottom",
      legend.title = element_text(size = 9, face = "bold"),
      legend.text = element_text(size = 9)
    )
  return(plot.theme)
}
