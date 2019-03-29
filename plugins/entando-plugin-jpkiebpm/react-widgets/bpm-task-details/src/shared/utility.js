export const updateObject = (oldObject, updatedProperties) => {
    return {
        ...oldObject,
        ...updatedProperties
    };
};


export const reformatWorkList = (worklist) => {
    var workListOut = [];
    for(var key in worklist){
        workListOut.push(worklist[key].kieTask);
    }
    return workListOut;
}