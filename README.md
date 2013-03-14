resourcelimitor
===============

a class to  limit the count of the threads that can access to some shared resource.

ResourceLimitor limitor = new ResourceLimitor();
if(limitor.applyResource()){
    try{
        method.invoke();
    }catch(Exception ex){
    
    }finally{
        limitor.releaseResource();
    }
}
