init.libraries <- function(configuration){
  if(configuration == "irr"){
    suppressPackageStartupMessages(library("irr"))
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
